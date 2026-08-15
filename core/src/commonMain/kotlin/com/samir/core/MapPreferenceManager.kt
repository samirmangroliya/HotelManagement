package com.samir.core

class MapPreferenceManager : PreferenceManager {
    private val map = mutableMapOf<String, Any>()

    override fun saveString(key: String, value: String) {
        map[key] = value
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return map[key] as? String ?: defaultValue
    }

    override fun saveInt(key: String, value: Int) {
        map[key] = value
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return map[key] as? Int ?: defaultValue
    }

    override fun saveBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return map[key] as? Boolean ?: defaultValue
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }
}
