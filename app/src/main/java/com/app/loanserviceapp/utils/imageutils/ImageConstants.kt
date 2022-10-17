package com.app.loanserviceapp.utils.imageutils

class ImageConstants {
    companion object {
        enum class IMAGE_EXTENTIONS(val value: String) {
            JPG(".jpg"),
            JPEG(".jpeg"),
            PNG(".png")
        }
        enum class THUMBNAIL(val height: Int, val width: Int) {
            VERY_HIGH(1920, 1080),
            HIGH(1280, 720),
            SEMI_MEDIUM(720, 480),
            MEDIUM(240, 180),
            LOW(120, 90)
        }
    }
}
