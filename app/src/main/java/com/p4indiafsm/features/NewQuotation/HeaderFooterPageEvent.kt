package com.p4indiafsm.features.NewQuotation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import com.p4indiafsm.R
import com.p4indiafsm.app.Pref
import com.p4indiafsm.app.utils.AppUtils
import com.itextpdf.text.*
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.IOException


class HeaderFooterPageEvent : PdfPageEventHelper() {

    override fun onStartPage(writer: PdfWriter?, document: Document?) {
        //super.onStartPage(writer, document)
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("Top Left"), 30f, 800f, 0f);
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("Top Right"), 550f, 800f, 0f);
    }

    override fun onEndPage(writer: PdfWriter?, document: Document?) {
        //super.onEndPage(writer, document)
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase(""), 110f, 30f, 0f);
        //ColumnText.showTextAligned(writer!!.getDirectContent(), Element.ALIGN_CENTER,  Phrase("page " + document!!.getPageNumber()), 550f, 30f, 0f);



        //Hardcoded for EuroBond
//        val bm: Bitmap = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.ics_image_full)
//          val bm: Bitmap = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.footer_icon_euro)
        var bm: Bitmap
        if(Pref.IsShowQuotationFooterforEurobond){
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.footer_icon_euro)
        }else{
            bm = BitmapFactory.decodeResource(AppUtils.contx!!.resources, R.drawable.strip_line)
        }

        val bitmap = Bitmap.createScaledBitmap(bm, 950, 80, true);
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var img: Image? = null
        val byteArray: ByteArray = stream.toByteArray()
        try {
            img = Image.getInstance(byteArray)
          //  img.scaleToFit(155f,90f)
            img.scalePercent(50f)
            img.alignment=Image.ALIGN_RIGHT
        } catch (e: BadElementException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var imgSoc:Image = Image.getInstance(img)
        //imgSoc.scaleToFit(150f,80f);
        //imgSoc.setAbsolutePosition(390f, 720f);
        imgSoc.setAbsolutePosition(20f, 10f);
        var cb : PdfContentByte = writer!!.getDirectContent() as PdfContentByte
        cb.addImage(imgSoc)



    }

}