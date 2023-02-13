package com.p4indiafsm.features.shopdetail.presentation.api.collectionlist

/**
 * Created by Saikat on 13-11-2018.
 */
object CollectionListRepoProvider {
    fun collectionListRepository(): CollectionListRepo {
        return CollectionListRepo(CollectionListApi.create())
    }
}