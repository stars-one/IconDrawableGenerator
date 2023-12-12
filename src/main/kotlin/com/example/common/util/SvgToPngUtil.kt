package com.example.common.util

import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.gvt.renderer.ImageRenderer
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.XMLResourceDescriptor
import org.apache.commons.codec.binary.Base64
import java.awt.Color
import java.awt.Dimension
import java.awt.RenderingHints
import java.io.*
import java.util.*
import kotlin.math.min

class SvgToPngUtil {
    private fun getScaledDimension(imageSize: Dimension, boundary: Dimension): Dimension {
        val widthRatio = boundary.getWidth() / imageSize.getWidth()
        val heightRatio = boundary.getHeight() / imageSize.getHeight()
        val ratio = min(widthRatio, heightRatio)
        return Dimension((imageSize.width.toDouble() * ratio).toInt(), (imageSize.height.toDouble() * ratio).toInt())
    }

    private fun hex2Rgb(colorStr: String?): Color {
        require(!(colorStr == null || colorStr.trim { it <= ' ' }
            .isEmpty())) { "The background color must be specified!!" }
        return if (!colorStr.trim { it <= ' ' }.startsWith("#")) {
            try {
                Color::class.java.getDeclaredField(colorStr.trim { it <= ' ' }
                    .uppercase(Locale.getDefault()))[null] as Color
            } catch (e: Exception) {
                throw RuntimeException(
                    "Could not parse color with name: '%s'. Maybe you want to provide the hex string value instead?? i.e. #ffffff"
                )
            }
        } else Color(
            colorStr.substring(1, 3).toInt(16),
            colorStr.substring(3, 5).toInt(16),
            colorStr.substring(5, 7).toInt(16)
        )
    }

    @Throws(Exception::class)
    private fun svgSize(svgText: String): Dimension {
        val parser = XMLResourceDescriptor.getXMLParserClassName()
        val f = SAXSVGDocumentFactory(parser)
        val doc = f.createDocument("", CharArrayReader(svgText.toCharArray()))
        val svgAttributes = doc.firstChild.attributes
        val widthText = svgAttributes.getNamedItem("width").textContent
        val heightText = svgAttributes.getNamedItem("height").textContent
        val w = widthText.toInt()
        val h = heightText.toInt()
        return Dimension(w, h)
    }

    @Throws(Exception::class)
    private fun toPngFromReader(
        r: Reader,
        resultByteStream: OutputStream,
        backgroundColor: String?,
        geometry: Dimension
    ) {
        val transcoderInput = TranscoderInput(r)
        val transcoderOutput = TranscoderOutput(resultByteStream)
        val pngTranscoder: PNGTranscoder = object : PNGTranscoder() {
            override fun createRenderer(): ImageRenderer {
                val r = super.createRenderer()
                val rh = r.renderingHints
                rh.add(
                    RenderingHints(
                        RenderingHints.KEY_ALPHA_INTERPOLATION,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
                    )
                )
                rh.add(
                    RenderingHints(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC
                    )
                )
                rh.add(RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON))
                rh.add(
                    RenderingHints(
                        RenderingHints.KEY_COLOR_RENDERING,
                        RenderingHints.VALUE_COLOR_RENDER_QUALITY
                    )
                )
                rh.add(RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE))
                rh.add(RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY))
                rh.add(RenderingHints(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE))
                rh.add(
                    RenderingHints(
                        RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON
                    )
                )
                rh.add(
                    RenderingHints(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                    )
                )
                r.renderingHints = rh
                return r
            }
        }
        if (backgroundColor != null && backgroundColor.trim { it <= ' ' }.length != 0) pngTranscoder.addTranscodingHint(
            PNGTranscoder.KEY_BACKGROUND_COLOR,
            hex2Rgb(backgroundColor.trim { it <= ' ' })
        )
        if (geometry.getWidth() > 1.0) {
            pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 180)
            pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 180)
        }
        pngTranscoder.transcode(transcoderInput, transcoderOutput)
    }

    @Throws(Exception::class)
    fun toPngFileFromString(svg: String, backgroundColor: String?, outputFile: String?, geometry: String?) {
        FileOutputStream(File(outputFile)).use { resultByteStream ->
            val imageBytes = toPngBytesFromString(svg, backgroundColor, geometry)
            resultByteStream.write(imageBytes)
        }
    }

    @Throws(Exception::class)
    fun toPngBase64FromString(svg: String, backgroundColor: String?, geometry: String?): String {
        val imageBytes = toPngBytesFromString(svg, backgroundColor, geometry)
        return Base64.encodeBase64String(imageBytes)
    }

    @Throws(Exception::class)
    fun toPngBytesFromString(svg: String, backgroundColor: String?, geometry: String?): ByteArray {
        ByteArrayOutputStream().use { bos ->
            //todo 设置宽高无用...现在默认是输出400*400图片
            var dim = Dimension(200, 200)
            toPngFromReader(StringReader(svg), bos, backgroundColor, dim)
            return bos.toByteArray()
        }
    }
}