rootProject.name = "twitch4j-clients"

include(
  ":http",
  ":websocket", //":websocket:jdk11", ":websocket:okhttp3", ":websocket:nv-websocket",
  ":mapper",
  ":event"
)
