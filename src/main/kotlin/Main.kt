package watermark

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {

    println("Input the image filename:")

    val path = readln()

    val file = File(path)

    if (!file.exists()) {
        println("The file $path doesn't exist.")
        return
    }

    val image = ImageIO.read(file)

    val imageHeight = image.height
    val imageWidth = image.width

    val numColorComponents = image.colorModel.numColorComponents
    if (numColorComponents != 3) {
        println("The number of image color components isn't 3.")
        return
    }

    val pixelSize = image.colorModel.pixelSize
    if (pixelSize != 24 && pixelSize != 32) {
        println("The image isn't 24 or 32-bit.")
        return
    }

    println("Input the watermark image filename:")

    val watermarkPath = readln()

    val watermarkFile = File(watermarkPath)

    if (!watermarkFile.exists()) {
        println("The file $watermarkFile doesn't exist.")
        return
    }

    val watermarkImage = ImageIO.read(watermarkFile)
    val watermarkHeight = watermarkImage.height
    val watermarkWidth = watermarkImage.width
    val watermarkNumColorComponents = watermarkImage.colorModel.numColorComponents
    val watermarkPixelSize = watermarkImage.colorModel.pixelSize

    if (watermarkNumColorComponents != 3) {
        println("The number of watermark color components isn't 3.")
        return
    }

    if (watermarkPixelSize != 24 && watermarkPixelSize != 32) {
        println("The watermark isn't 24 or 32-bit.")
        return
    }

    if (watermarkHeight > imageHeight || watermarkWidth > imageWidth) {
        println("The watermark's dimensions are larger.")
        return
    }

    if(watermarkImage.transparency == Transparency.TRANSLUCENT){
        println("Do you want to use the watermark's Alpha channel?")
        if(readln().lowercase() == "yes"){

            println("Input the watermark transparency percentage (Integer 0-100):")
            val weight = readln().toIntOrNull()
            if (weight == null) {
                println("The transparency percentage isn't an integer number.")
                return
            }

            if (weight < 0 || weight > 100) {
                println("The transparency percentage is out of range.")
                return
            }

            println("Choose the position method (single, grid):")
            val pos = readln().lowercase()

            if(pos != "single" && pos != "grid") {
                println("The position method input is invalid.")
                return
            }

            if(pos == "single"){
                val diffX = imageWidth - watermarkWidth
                val diffY = imageHeight - watermarkHeight
                println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
                val xy = readln().split(" ")
                if(xy.size != 2){
                    println("The position input is invalid.")
                    return
                }
                val xmargin = xy[0].toIntOrNull()
                val ymargin = xy[1].toIntOrNull()
                if(xmargin == null || ymargin == null){
                    println("The position input is invalid.")
                    return
                }
                if(xmargin > diffX || ymargin > diffY || xmargin < 0 || ymargin < 0){
                    println("The position input is out of range.")
                    return
                }

                println("Input the output image filename (jpg or png extension):")
                val output = readln()
                val outputFile = File(output)
                if (outputFile.extension != "png" && outputFile.extension != "jpg") {
                    println("The output file extension isn't \"jpg\" or \"png\".")
                }

                val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

                for (x in 0 until imageWidth){
                    for (y in 0 until imageHeight) {
                        if(x >= xmargin && y >= ymargin && x < xmargin + watermarkWidth && y < ymargin + watermarkHeight) {
                            val i = Color(image.getRGB(x, y), true)
                            val w = Color(watermarkImage.getRGB(x - xmargin, y - ymargin), true)
                            if(w.alpha == 0){
                                outputImage.setRGB(x, y, i.rgb)
                            } else {
                                val color = Color(
                                    (weight * w.red + (100 - weight) * i.red) / 100,
                                    (weight * w.green + (100 - weight) * i.green) / 100,
                                    (weight * w.blue + (100 - weight) * i.blue) / 100
                                )
                                outputImage.setRGB(x, y, color.rgb)
                            }
                        } else {
                            val i = Color(image.getRGB(x, y), true)
                            outputImage.setRGB(x, y, i.rgb)
                        }
                    }
                }

                ImageIO.write(outputImage, outputFile.extension, outputFile)

                println("The watermarked image $output has been created.")
                return

            }

            println("Input the output image filename (jpg or png extension):")
            val output = readln()
            val outputFile = File(output)
            if (outputFile.extension != "png" && outputFile.extension != "jpg") {
                println("The output file extension isn't \"jpg\" or \"png\".")
            }

            val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

            for (x in 0 until imageWidth){
                for (y in 0 until imageHeight) {
                    val i = Color(image.getRGB(x, y), true)
                    val w = Color(watermarkImage.getRGB(x % watermarkWidth, y % watermarkHeight), true)
                    if(w.alpha == 0){
                        outputImage.setRGB(x, y, i.rgb)
                    } else {
                        val color = Color(
                            (weight * w.red + (100 - weight) * i.red) / 100,
                            (weight * w.green + (100 - weight) * i.green) / 100,
                            (weight * w.blue + (100 - weight) * i.blue) / 100
                        )
                        outputImage.setRGB(x, y, color.rgb)
                    }
                }
            }

            ImageIO.write(outputImage, outputFile.extension, outputFile)

            println("The watermarked image $output has been created.")
            return


        } else {
            println("Input the watermark transparency percentage (Integer 0-100):")
            val weight = readln().toIntOrNull()
            if (weight == null) {
                println("The transparency percentage isn't an integer number.")
                return
            }

            if (weight < 0 || weight > 100) {
                println("The transparency percentage is out of range.")
                return
            }


            println("Choose the position method (single, grid):")
            val pos = readln().lowercase()

            if(pos != "single" && pos != "grid") {
                println("The position method input is invalid.")
                return
            }

            if(pos == "single"){
                val diffX = imageWidth - watermarkWidth
                val diffY = imageHeight - watermarkHeight
                println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
                val xy = readln().split(" ")
                if(xy.size != 2){
                    println("The position input is invalid.")
                    return
                }
                val xmargin = xy[0].toIntOrNull()
                val ymargin = xy[1].toIntOrNull()
                if(xmargin == null || ymargin == null){
                    println("The position input is invalid.")
                    return
                }
                if(xmargin > diffX || ymargin > diffY || xmargin < 0 || ymargin < 0){
                    println("The position input is out of range.")
                    return
                }

                println("Input the output image filename (jpg or png extension):")
                val output = readln()
                val outputFile = File(output)
                if (outputFile.extension != "png" && outputFile.extension != "jpg") {
                    println("The output file extension isn't \"jpg\" or \"png\".")
                }

                val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

                for (x in 0 until imageWidth){
                    for (y in 0 until imageHeight) {
                        if(x >= xmargin && y >= ymargin && x < xmargin + watermarkWidth && y < ymargin + watermarkHeight) {
                            val i = Color(image.getRGB(x, y))
                            val w = Color(watermarkImage.getRGB(x - xmargin, y - ymargin))
                            val color = Color(
                                (weight * w.red + (100 - weight) * i.red) / 100,
                                (weight * w.green + (100 - weight) * i.green) / 100,
                                (weight * w.blue + (100 - weight) * i.blue) / 100
                            )
                            outputImage.setRGB(x, y, color.rgb)
                        } else {
                            val i = Color(image.getRGB(x, y))
                            outputImage.setRGB(x, y, i.rgb)
                        }
                    }
                }

                ImageIO.write(outputImage, outputFile.extension, outputFile)

                println("The watermarked image $output has been created.")
                return

            }

            println("Input the output image filename (jpg or png extension):")
            val output = readln()
            val outputFile = File(output)
            if (outputFile.extension != "png" && outputFile.extension != "jpg") {
                println("The output file extension isn't \"jpg\" or \"png\".")
            }

            val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

            for (x in 0 until imageWidth){
                for (y in 0 until imageHeight) {
                    val i = Color(image.getRGB(x, y), true)
                    val w = Color(watermarkImage.getRGB(x % watermarkWidth, y % watermarkHeight), true)

                    val color = Color(
                        (weight * w.red + (100 - weight) * i.red) / 100,
                        (weight * w.green + (100 - weight) * i.green) / 100,
                        (weight * w.blue + (100 - weight) * i.blue) / 100
                    )
                    outputImage.setRGB(x, y, color.rgb)

                }
            }

            ImageIO.write(outputImage, outputFile.extension, outputFile)

            println("The watermarked image $output has been created.")
            return


        }
    }
    println("Do you want to set a transparency color?")
    if(readln().lowercase() == "yes") {
        println("Input a transparency color ([Red] [Green] [Blue]):")
        val rgb = readln().split(" ")
        if (rgb.size != 3) {
            println("The transparency color input is invalid.")
            return
        }
        val r = rgb[0].toIntOrNull()
        val g = rgb[1].toIntOrNull()
        val b = rgb[2].toIntOrNull()
        if (r == null || g == null || b == null) {
            println("The transparency color input is invalid.")
            return
        }
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            println("The transparency color input is invalid.")
            return
        }
        val transparencyColor = Color(r, g, b)

        println("Input the watermark transparency percentage (Integer 0-100):")
        val weight = readln().toIntOrNull()
        if (weight == null) {
            println("The transparency percentage isn't an integer number.")
            return
        }

        if (weight < 0 || weight > 100) {
            println("The transparency percentage is out of range.")
            return
        }


        println("Choose the position method (single, grid):")
        val pos = readln().lowercase()

        if(pos != "single" && pos != "grid") {
            println("The position method input is invalid.")
            return
        }

        if(pos == "single"){
            val diffX = imageWidth - watermarkWidth
            val diffY = imageHeight - watermarkHeight
            println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
            val xy = readln().split(" ")
            if(xy.size != 2){
                println("The position input is invalid.")
                return
            }
            val xmargin = xy[0].toIntOrNull()
            val ymargin = xy[1].toIntOrNull()
            if(xmargin == null || ymargin == null){
                println("The position input is invalid.")
                return
            }
            if(xmargin > diffX || ymargin > diffY || xmargin < 0 || ymargin < 0){
                println("The position input is out of range.")
                return
            }

            println("Input the output image filename (jpg or png extension):")
            val output = readln()
            val outputFile = File(output)
            if (outputFile.extension != "png" && outputFile.extension != "jpg") {
                println("The output file extension isn't \"jpg\" or \"png\".")
            }

            val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

            for (x in 0 until imageWidth){
                for (y in 0 until imageHeight) {
                    if(x >= xmargin && y >= ymargin && x < xmargin + watermarkWidth && y < ymargin + watermarkHeight) {
                        val i = Color(image.getRGB(x, y))
                        val w = Color(watermarkImage.getRGB(x - xmargin, y - ymargin))
                        if(w == transparencyColor){
                            outputImage.setRGB(x, y, i.rgb)
                        } else {
                            val color = Color(
                                (weight * w.red + (100 - weight) * i.red) / 100,
                                (weight * w.green + (100 - weight) * i.green) / 100,
                                (weight * w.blue + (100 - weight) * i.blue) / 100
                            )
                            outputImage.setRGB(x, y, color.rgb)
                        }
                    } else {
                        val i = Color(image.getRGB(x, y))
                        outputImage.setRGB(x, y, i.rgb)
                    }
                }
            }

            ImageIO.write(outputImage, outputFile.extension, outputFile)

            println("The watermarked image $output has been created.")
            return

        }

        println("Input the output image filename (jpg or png extension):")
        val output = readln()
        val outputFile = File(output)
        if (outputFile.extension != "png" && outputFile.extension != "jpg") {
            println("The output file extension isn't \"jpg\" or \"png\".")
        }

        val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

        for (x in 0 until imageWidth){
            for (y in 0 until imageHeight) {
                val i = Color(image.getRGB(x, y), true)
                val w = Color(watermarkImage.getRGB(x % watermarkWidth, y % watermarkHeight), true)
                if(w == transparencyColor){
                    outputImage.setRGB(x, y, i.rgb)
                } else {
                    val color = Color(
                        (weight * w.red + (100 - weight) * i.red) / 100,
                        (weight * w.green + (100 - weight) * i.green) / 100,
                        (weight * w.blue + (100 - weight) * i.blue) / 100
                    )
                    outputImage.setRGB(x, y, color.rgb)
                }
            }
        }

        ImageIO.write(outputImage, outputFile.extension, outputFile)

        println("The watermarked image $output has been created.")
        return

    }
    println("Input the watermark transparency percentage (Integer 0-100):")
    val weight = readln().toIntOrNull()
    if (weight == null) {
        println("The transparency percentage isn't an integer number.")
        return
    }

    if (weight < 0 || weight > 100) {
        println("The transparency percentage is out of range.")
        return
    }

    println("Choose the position method (single, grid):")
    val pos = readln().lowercase()

    if(pos != "single" && pos != "grid") {
        println("The position method input is invalid.")
        return
    }

    if(pos == "single"){
        val diffX = imageWidth - watermarkWidth
        val diffY = imageHeight - watermarkHeight
        println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
        val xy = readln().split(" ")
        if(xy.size != 2){
            println("The position input is invalid.")
            return
        }
        val xmargin = xy[0].toIntOrNull()
        val ymargin = xy[1].toIntOrNull()
        if(xmargin == null || ymargin == null){
            println("The position input is invalid.")
            return
        }
        if(xmargin > diffX || ymargin > diffY || xmargin < 0 || ymargin < 0){
            println("The position input is out of range.")
            return
        }

        println("Input the output image filename (jpg or png extension):")
        val output = readln()
        val outputFile = File(output)
        if (outputFile.extension != "png" && outputFile.extension != "jpg") {
            println("The output file extension isn't \"jpg\" or \"png\".")
        }

        val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

        for (x in 0 until imageWidth){
            for (y in 0 until imageHeight) {
                if(x >= xmargin && y >= ymargin && x < xmargin + watermarkWidth && y < ymargin + watermarkHeight) {
                    val i = Color(image.getRGB(x, y), true)
                    val w = Color(watermarkImage.getRGB(x - xmargin, y - ymargin),true)

                    val color = Color(
                        (weight * w.red + (100 - weight) * i.red) / 100,
                        (weight * w.green + (100 - weight) * i.green) / 100,
                        (weight * w.blue + (100 - weight) * i.blue) / 100
                    )
                    outputImage.setRGB(x, y, color.rgb)
                } else {
                    val i = Color(image.getRGB(x, y), true)
                    outputImage.setRGB(x, y, i.rgb)
                }
            }
        }

        ImageIO.write(outputImage, outputFile.extension, outputFile)

        println("The watermarked image $output has been created.")
        return

    }

    println("Input the output image filename (jpg or png extension):")
    val output = readln()
    val outputFile = File(output)
    if (outputFile.extension != "png" && outputFile.extension != "jpg") {
        println("The output file extension isn't \"jpg\" or \"png\".")
    }

    val outputImage = BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB)

    for (x in 0 until imageWidth){
        for (y in 0 until imageHeight) {
            val i = Color(image.getRGB(x, y), true)
            val w = Color(watermarkImage.getRGB(x % watermarkWidth, y % watermarkHeight), true)

            val color = Color(
                (weight * w.red + (100 - weight) * i.red) / 100,
                (weight * w.green + (100 - weight) * i.green) / 100,
                (weight * w.blue + (100 - weight) * i.blue) / 100
            )
            outputImage.setRGB(x, y, color.rgb)

        }
    }

    ImageIO.write(outputImage, outputFile.extension, outputFile)

    println("The watermarked image $output has been created.")
    return
}