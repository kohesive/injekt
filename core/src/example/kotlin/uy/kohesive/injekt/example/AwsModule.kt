package uy.kohesive.injekt.example

import uy.kohesive.injekt.api.*

// our module exports an injectable singletone for an Amazon S3 client, perfectly configured!

object AmazonS3InjektModule : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
        addSingletonFactory { AmazonS3Client(defaultCredentialsProviderChain()) }
    }
}


// mock classes

private fun defaultCredentialsProviderChain(): AWSCredentialsProviderChain { return AWSCredentialsProviderChain() }

class AWSCredentialsProviderChain()
data class AmazonS3Client(val providerChain: AWSCredentialsProviderChain)