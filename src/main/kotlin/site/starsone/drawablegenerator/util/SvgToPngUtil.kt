package site.starsone.drawablegenerator.util

import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.gvt.renderer.ImageRenderer
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.SVGConstants
import org.apache.batik.util.XMLResourceDescriptor
import org.apache.commons.codec.binary.Base64
import java.awt.Color
import java.awt.Dimension
import java.awt.RenderingHints
import java.io.*
import java.util.*
import kotlin.math.min

class SvgToPngUtil {

    @Throws(Exception::class)
    private fun toPngFromReader(
        r: Reader,
        resultByteStream: OutputStream,
        backgroundColor: Color,
        width:Int=200,
        height:Int=200
    ) {
        val transcoderInput = TranscoderInput(r)
        val transcoderOutput = TranscoderOutput(resultByteStream)

        //设置图片输出的宽高
        val pngTranscoder = MyPNGTranscoder(width,height)

        //设置背景色
        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, backgroundColor)
        pngTranscoder.transcode(transcoderInput, transcoderOutput)
    }

    @Throws(Exception::class)
    fun toPngFileFromString(svg: String, backgroundColor: Color, outputFile: File,width:Int=200, height:Int=200) {
        outputFile.outputStream().use { resultByteStream ->

            //得到png的二进制数据
            val imageBytes = ByteArrayOutputStream().use { bos ->
                toPngFromReader(svg.reader(), bos, backgroundColor,width,height)
                bos.toByteArray()
            }

            //输出文件
            resultByteStream.write(imageBytes)
        }
    }
}

class MyPNGTranscoder(myWidth:Int,myHeight:Int):PNGTranscoder(){
    init {
        this.width = myWidth.toFloat()
        this.height = myHeight.toFloat()
    }

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