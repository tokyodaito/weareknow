package com.bogsnebes.weareknow.accessibility.action

object ActionSaver {
    //DB connection

    fun save(options: List<String>, filter: (String) -> Boolean = { it != "" }) {
        println(ActionBuilder.createAction(options.filter(filter)))
    }
}