package com.fake.information.sever.demo

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication: ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        println("""
  ______      _          _   _                       _____     _               _    _   __        
 |  ____|    | |        | \ | |                     |_   _|   | |             | |  (_) / _|       
 | |__  __ _ | | __ ___ |  \| |  ___ __      __ ___   | |   __| |  ___  _ __  | |_  _ | |_  _   _ 
 |  __|/ _` || |/ // _ \| . ` | / _ \\ \ /\ / // __|  | |  / _` | / _ \| '_ \ | __|| ||  _|| | | |
 | |  | (_| ||   <|  __/| |\  ||  __/ \ V  V / \__ \ _| |_| (_| ||  __/| | | || |_ | || |  | |_| |
 |_|   \__,_||_|\_\\___||_| \_| \___|  \_/\_/  |___/|_____|\__,_| \___||_| |_| \__||_||_|   \__, |
                                                                                             __/ |
                                                                                            |___/ 
        """.trimIndent())

    }
}
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

