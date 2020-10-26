# OpenIndustrialization


## Setting up run configurations

Main Class: `net.fabricmc.devlaunchinjector.Main`

VM Options (Replace %PROJECT_PATH% with the directory you cloned the repo to):
```
-Dfabric.dli.config=%PROJECT_PATH%\Minecraft\.gradle\loom-cache\launch.cfg -Dfabric.dli.env=client -Dfabric.dli.main=net.fabricmc.loader.launch.knot.KnotClient
```

Module: `OpenIndustrialization.Minecraft.main`