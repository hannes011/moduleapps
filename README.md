ModuleApps - A simple Plugin Injection Framework
================================================
*by Hannes Dr*

This simple framework connects the advantage of package managers like [Maven](https://maven.apache.org/) (or any other)
with the Java plugin loader [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html)
and a clean [SOLID](https://en.wikipedia.org/wiki/SOLID) design pattern by
providing a dynamic way of [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection).


### Example ###

ExampleApp extends AbstractApp implements App
+ main(): void

DataSourceModel extends Module
+ loadData(Class<FORMAT>): FORMAT

PrettyPrinterModel extends Module
+ getPrettyPrinter

PrettyPrinter<FROMAT> extends Service<String>
+ prettify(FORMAT): String

XmlPrettyPrinter extends PrettyPrinter<Document>
+ prettify(Document): String

JsonPrettyPrinter extends PrettyPrinter<String>
+ prettify(String): String

-> depends on JsonParser which is provided via ParserModule so you can introduce new interfaces without touching the dependency injection implementation of your application. You simply need to adjust your pom.xml
