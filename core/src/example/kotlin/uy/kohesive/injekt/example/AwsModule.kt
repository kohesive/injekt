package uy.kohesive.injekt.example

import uy.kohesive.injekt.*

// our module exports an injektable singletone for an Amazon S3 client, perfectly configured!

public object AmazonS3InjektModule : InjektModule {
    override fun InjektRegistrar.exportInjektables() {
        addSingletonFactory { AmazonS3Client(defaultCredentialsProviderChain()) }
    }
}


// mock classes

private fun defaultCredentialsProviderChain(): AWSCredentialsProviderChain { return AWSCredentialsProviderChain() }

data class AWSCredentialsProviderChain()
data class AmazonS3Client(providerChain: AWSCredentialsProviderChain)