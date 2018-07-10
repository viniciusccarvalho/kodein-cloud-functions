val kodein = Kodein {
        loadModules().forEach { module -> import(module) }
        bind<ObjectMapper>() with singleton { ObjectMapper().registerKotlinModule() }
    }