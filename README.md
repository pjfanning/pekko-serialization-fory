# pekko-serialization-jackson216

Jackson 2.16 equivalent of [pekko-serialization-jackson](https://pekko.apache.org/docs/pekko/current/serialization-jackson.html) which uses Jackson 2.14.

See also https://github.com/pjfanning/pekko-serialization-jackson215

The main reason to use Jackson 2.16 is for its [StreamReadConstraint](https://www.javadoc.io/static/com.fasterxml.jackson.core/jackson-core/2.16.0/com/fasterxml/jackson/core/StreamReadConstraints.html) support. Users who want to override the default constraints should override the settings in the [config](https://github.com/lightbend/config), see [reference.conf](https://github.com/pjfanning/pekko-serialization-jackson216/blob/main/src/main/resources/reference.conf).

Config names for this library start with `pekko.serialization.jackson216` as opposed to `pekko.serialization.jackson`.

If you want to use Jackson 2.16 with [Pekko HTTP](https://pekko.apache.org/docs/pekko-http/current), use [pekko-http-jackson](https://github.com/pjfanning/pekko-http-json) v2.3.0 instead.

## sbt

```
libraryDependencies += "com.github.pjfanning" %% "pekko-serialization-jackson216" % "1.0.1"
```
