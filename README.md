# Lumina

Mappings utilized by OmniMC. The Writer component is exclusively employed for uploading LuminaMappings to the site.

This project is still working on some things.

# Importing

### Maven

* Include JitPack in your maven build file

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

* Add ASMHelper as a dependency

```xml
<dependency>
    <groupId>com.github.OmniModLoader</groupId>
    <artifactId>Lumina</artifactId>
    <version>1.2.5</version>
</dependency>
```

### Gradle

* Add JitPack to your root `build.gradle` at the end of repositories

```gradle
repositories {
    maven {
        url 'https://jitpack.io'
    }
}
```

* Add the dependency

```gradle
dependencies {
    implementation 'com.github.OmniModLoader:Lumina:1.2.5'
}
```

# License

[Lumina is licensed under MIT](./LICENSE).

# Building

```shell
mvm package
```