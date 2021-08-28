package com.example.mycinemaapp.model

data class Cinema(
    val film: Film = getDefaultFilm(),
    val rating: Double = 5.5,
    )

fun getDefaultFilm() = Film("Бегущий в лабиринте", 2017, "Группа подростков выживает в лабиринте")
