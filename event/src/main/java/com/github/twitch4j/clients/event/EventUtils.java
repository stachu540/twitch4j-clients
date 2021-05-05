package com.github.twitch4j.clients.event;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unchecked")
public final class EventUtils {

  private static final Type<Consumer<? extends Event>> DEFAULT_CONSUMER = new Type<Consumer<? extends Event>>();

  static Map<Class<? extends Event>, Consumer<? extends Event>> fetchEvents(Object eventHandler) {
    if (DEFAULT_CONSUMER.raw.isAssignableFrom(eventHandler.getClass())) {
      Class<Consumer<? extends Event>> consumerType = (Class<Consumer<? extends Event>>) eventHandler.getClass();
      Class<? extends Event> type = new Type<>((Class<? extends Event>) ((ParameterizedType) consumerType.getGenericSuperclass()).getActualTypeArguments()[0]).getRaw();
      return Collections.singletonMap(type, (Consumer<? extends Event>) eventHandler);
    } else {
      Map<Class<? extends Event>, Consumer<? extends Event>> events = new LinkedHashMap<>();
      for (Method m : eventHandler.getClass().getMethods()) {
        if (m.isAnnotationPresent(EventHandler.class)) {
          Map.Entry<Class<? extends Event>, Consumer<Event>> entry = composeEvent(eventHandler, m);
          Consumer<? extends Event> handler = entry.getValue();
          if (events.containsKey(entry.getKey())) {
            handler = events.get(entry.getKey()).andThen((Consumer<Event>) handler);
          }
          events.put(entry.getKey(), handler);
        }
      }
      return events;
    }
  }

  private static Map.Entry<Class<? extends Event>, Consumer<Event>> composeEvent(Object eventHandler, Method method) {
    EventHandler handler = method.getAnnotation(EventHandler.class);
    Class<? extends Event> eventType = handler.value();
    if (eventType == EventHandler.Void.class && method.getParameterCount() == 1) {
      Class<?> rawParameterType = method.getParameterTypes()[0];
      if (rawParameterType.isAssignableFrom(Event.class)) {
        eventType = (Class<? extends Event>) rawParameterType;
      }
    } else {
      throw new IllegalArgumentException(String.format("The method \"%s\" doesn't contains implements Event.class", method.getName()));
    }
    final Class<? extends Event> thatEvent = eventType;

    return new AbstractMap.SimpleImmutableEntry<>(thatEvent, event -> {
      if (thatEvent.isInstance(event)) {
        try {
          if (method.getParameterTypes()[0].isInstance(event)) {
            method.invoke(eventHandler, event);
          } else {
            Object[] parameters = Arrays.stream(method.getParameters())
              .map(parameter -> {
                try {
                  String name = getter(parameter.getName());
                  Class<?> type = parameter.getType();
                  Method parameterMethod = event.getClass().getMethod(name);
                  if (parameterMethod.getReturnType().isAssignableFrom(type)) {
                    return parameterMethod.invoke(event);
                  } else {
                    return null;
                  }
                } catch (ReflectiveOperationException ignore) {
                  return null;
                }
              }).toArray();
            method.invoke(eventHandler, parameters);
          }
        } catch (ReflectiveOperationException ignore) {

        }
      }
    });
  }

  private static String getter(String name) {
    return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
  }

  @Getter
  @RequiredArgsConstructor
  static class Type<T> {
    private final Class<T> raw;

    Type() {
      this.raw = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
  }
}
