package com.example.saveit.ui.ahorro

import java.time.LocalDateTime


data class Ahorro(val monto:Float,
                  val ingreso:Boolean,
                  val fecha:LocalDateTime
)