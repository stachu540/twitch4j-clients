package com.github.twitch4j.clients.event;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public final class EventUtils {
  static Map<Class<? extends Event>, Consumer<Event>> fetchEvents(Object eventHandler) {
    Map<Class<? extends Event>, Consumer<Event>> events = new LinkedHashMap<>();
    if (eventHandler instanceof Consumer) {
      Arrays.stream(eventHandler.getClass().getGenericInterfaces())
        .map(t -> (ParameterizedType) t).filter(t -> ((Class<?>) t.getRawType()).isAssignableFrom(Consumer.class))
        .map(t -> (Class<?>) t.getActualTypeArguments()[0])
        .filter(Event.class::isAssignableFrom).map(c -> (Class<? extends Event>) c)
        .forEach(c -> putEvent(events, c, (Consumer<Event>) eventHandler));
    }
    Arrays.stream(eventHandler.getClass().getMethods()).filter(m -> m.isAnnotationPresent(EventHandler.class) && m.getParameterCount() > 0)
      .forEach(m -> {
        int paramCount = m.getParameterCount();
        if (paramCount == 1 && Event.class.isAssignableFrom(m.getParameterTypes()[0])) {
          putEvent(events, (Class<? extends Event>) m.getParameterTypes()[0], event -> {
            try {
              m.invoke(eventHandler, event);
            } catch (ReflectiveOperationException e) {
              throw new IllegalCallerException("Cannot invoke this event", e);
            }
          });
        } else {
          Class<? extends Event> type = m.getAnnotation(EventHandler.class).value();
          if (!EventHandler.Void.class.isAssignableFrom(type)) {
            putEvent(events, type, event -> {
              Object[] parameters = Arrays.stream(m.getParameters()).map(parameter -> {
                if (type.isAssignableFrom(parameter.getType())) {
                  return event;
                } else if (parameter.isAnnotationPresent(EventParameter.class)) {
                  try {
                    String name = getter(parameter.getAnnotation(EventParameter.class).value());
                    return event.getClass().getMethod(name).invoke(event);
                  } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Cannot get access to this method.", e);
                  }
                } else {
                  throw new IllegalArgumentException("Cannot translate this parameter. It must be a Event or his parameter (without get prefix - ex. requesting \"simpleName\" will calls \"getSimpleName()\")");
                }
              }).toArray();
              try {
                m.invoke(eventHandler, parameters);
              } catch (ReflectiveOperationException e) {
                throw new IllegalCallerException("Cannot invoke this event", e);
              }
            });
          }
        }
      });
    return events;
  }

  private static void putEvent(Map<Class<? extends Event>, Consumer<Event>> events, Class<? extends Event> eventType, Consumer<Event> handler) {
    if (events.containsKey(eventType)) {
      handler = events.get(eventType).andThen(handler);
    }
    events.put(eventType, handler);
  }

  private static String getter(String name) {
    return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
  }

  private static <T> Class<T> getActualTypeArguments(Class<?> type, int index) {
    java.lang.reflect.Type generic = type.getGenericSuperclass();
    if (generic instanceof ParameterizedType) {
      return (Class<T>) ((ParameterizedType) generic).getActualTypeArguments()[index];
    }
    return null;
  }
}
