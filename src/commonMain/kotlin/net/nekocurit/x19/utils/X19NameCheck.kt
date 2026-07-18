package net.nekocurit.x19.utils

object X19NameCheck {

    /**
     * 检查名称是否符合《我的世界 中国版》标准
     *
     * 已实现的检查: 长度 [wiki](https://zh.minecraft.wiki/w/%E4%B8%AD%E5%9B%BD%E7%89%88#%E7%8E%A9%E5%AE%B6%E6%A0%87%E8%AF%86), 符号
     */
    fun check(name: String) = name
        // 是否包含符号
        .takeIf { it.all { char -> char == '_' || char.isLetterOrDigit() } }
        // 长度检查 数字字母=1 中文=1.5
        ?.sumOf { char -> if (char.code <= 0x7F) 1.0 else 1.5 }
        ?.let { it in 3.0..12.0 }
        ?: false

}