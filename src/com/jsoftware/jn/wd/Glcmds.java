package com.jsoftware.jn.wd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.JView;
import java.lang.Character;
import java.lang.System;
import java.nio.charset.Charset;

//complex unit (sp, dip, pt, px, mm, in)

public class Glcmds
{

  JView view;

  static Context context;
  static Font FontExtent;
  static float point;

  Font font;
  Canvas canvas;
  Paint paint;
  Path path;
  int RGBSEQ ;

  int    andclipped;
  int    andrgb;
  int    andtextx;
  int    andtexty;
  int    andunderline;
  int    andfontangle;
  int    andpenrgb;
  int    andbrushrgb;
  int    andtextrgb;
  int    andbrushnull;
  int    andorgx;
  int    andorgy;
  int    nodoublebuf=1;

  float[] tarc = new float [8];


  public Glcmds(JView view)
  {
    this.view=view;
    path=new Path();
    paint=new Paint();
    paint.setAntiAlias(true);
    context=JConsoleApp.theApp.getApplicationContext();
  }

  void tors (float [] z)
  {
    z[2] = z[0] + z[2];
    z[3] = z[1] + z[3];
  }

  private void androidarcisi (float[] v)
  {
    float xc,yc,a,b;
    float ang1,ang2;
    xc = v[0] + 0.5f*v[2];
    yc = v[1] + 0.5f*v[3];
    tors(v);

    a = v[4] - xc;
    b = v[5] - yc;
    if(0==a) ang1 = 90f * Math.signum(b);
    else ang1 = (180 / (float)Math.PI) * (float)Math.atan(b/a);
    if ((0<=ang1) && (a<0)) ang1 = 180 + ang1;
    else if ((0>ang1) && (a<0)) ang1 = 180 + ang1;

    a = v[6] - xc;
    b = v[7] - yc;
    if(0==a) ang2 = 90f * Math.signum(b);
    else ang2 = (180 / (float)Math.PI) * (float)Math.atan(b/a);
    if ((0<=ang2) && (a<0)) ang2 = 180 + ang2;
    else if ((0>ang2) && (a<0)) ang2 = 180 + ang2;

    v[4] = ang2;
    if (0>ang1-ang2) v[5] = 360+(ang1-ang2);
    else v[5] = ang1-ang2;
  }

  private void fliprgb (int[] da, int cnt)
  {
    int i,a;
    for (i = 0; i < cnt; i++) {
      da[i] = Integer.rotateRight(Integer.reverseBytes(da[i]), 8);
    }
  }

  public int glfont0(String s)
  {
    Font fnt=new Font(s);
    if (fnt.error) {
      return 1;
    }
    font=fnt;
    paint.setTypeface(font.font);
    point=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, font.fontsize, context.getResources().getDisplayMetrics());
    paint.setTextSize(point);
// also set glfontextent
    FontExtent=new Font(s);
    return 0;
  }

  public int glclear2(boolean clear)
  {
    if (null!=canvas) {
      canvas.translate(-andorgx,-andorgy);
      canvas.clipRect(0,0,view.getWidth(),view.getHeight(),Region.Op.REPLACE);
    }
    andrgb = Color.argb(255,0,0,0);
    if (0!=glfont0("profont")) return 1;
    andunderline = 0;
    andfontangle = 0;
    andclipped= 0;
    andorgx = 0;
    andorgy = 0;
    andpenrgb = andrgb;
    andbrushrgb = andrgb;
    paint.setStrokeWidth(2f);
    paint.setARGB(255,255,255,255);
    paint.setStyle(Paint.Style.FILL);
    if (null!=canvas) {
      if (clear) canvas.drawRect(0,0,view.getWidth(),view.getHeight(),paint);
    }
    andbrushnull = 1;
    andtextx = 0;
    andtexty = 0;
    andtextrgb = andrgb;
    return 0;
  }

  public static int[] qextent ( String text)
  {
    Paint paint=new Paint();
    if (null==FontExtent) FontExtent=new Font("profont");
    paint.setTypeface(FontExtent.font);
    point=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, FontExtent.fontsize, context.getResources().getDisplayMetrics());
    paint.setTextSize(point);
    Rect rect = new Rect();
    int[] wlen = new int[2];

    paint.getTextBounds(text,0,text.length(),rect);
    wlen[0] = rect.width();
    wlen[1] = rect.height();
    return wlen;
  }

  public static int[] qextentw ( String[] text)
  {
    int ncnt=text.length;
    Paint paint=new Paint();
    if (null==FontExtent) FontExtent=new Font("profont");
    paint.setTypeface(FontExtent.font);
    point=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, FontExtent.fontsize, context.getResources().getDisplayMetrics());
    paint.setTextSize(point);
    Rect rect = new Rect();
    int[] wlen = new int[ncnt];

    for (int i=0; i<ncnt; i++) {
      paint.getTextBounds(text[i],0,text[i].length(),rect);
      wlen[i] = rect.width();
    }
    return wlen;
  }

  public int glcmds ( int[] buf , Object[] res)
  {

    int[] result=new int[0];
    String textstring;
    int[] intbuf;
    Rect rect=new Rect();
    RectF rectf;
    Font fnt;
    String face;
    int errcnt =  0;
    int ncnt =  buf.length;
    int cmd;
    int p =  0;
    int i,c,cnt;
    int a;
    int b;
    int w;
    int h;
    Paint tpaint;

//    Log.d(JConsoleApp.LogTag, "Glcmds ncnt: " + Integer.toString(ncnt));
    while ((0>=errcnt) && (p<ncnt)) {
      errcnt=0;
//      Log.d(JConsoleApp.LogTag, "Glcmds p: " + Integer.toString(p));
      cnt =  buf[p];
      cmd =  buf[p+1];
//      Log.d(JConsoleApp.LogTag, "Glcmds cnt: " + Integer.toString(cnt));
//      Log.d(JConsoleApp.LogTag, "Glcmds cmd: " + Integer.toString(cmd));
      switch (cmd) {

      case 2001 : // glarc
        if (null==canvas) {
          errcnt=1;
          break;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(andpenrgb);
        for (i=0; i<8; i++)tarc[i] = buf[p + 2 + i];
        androidarcisi(tarc);
        rectf = new RectF(tarc[0],tarc[1],tarc[2],tarc[3]);
        canvas.drawArc ( rectf, tarc[4], tarc[5], false, paint);
        rectf = null;
        break;

      case 2004 :    // glbrush
        andbrushrgb = andrgb;
        andbrushnull = 0;
        break;

      case 2005 :    // glbrushnull
        andbrushnull = 1;
        break;

      case 2007 : // glclear

        FontExtent=null;
        errcnt=glclear2(true);
        break;

      case 2078 : // glclip

        if (null==canvas) {
          errcnt=1;
          break;
        }
        andclipped= 1;
        canvas.clipRect(buf[p + 2],buf[p + 3],buf[p + 2]+buf[p + 4],buf[p + 3]+buf[p + 5], Region.Op.REPLACE);
        break;

      case 2079 : // glclipreset

        if (null==canvas) {
          errcnt=1;
          break;
        }
        andclipped= 0;
        canvas.clipRect(0,0,view.getWidth(),view.getHeight(),Region.Op.REPLACE);
        break;

      case 2999 : // glcmds
        intbuf = new int[cnt-2];
        System.arraycopy(buf, p+2, intbuf, 0, cnt-2);
        errcnt=glcmds(intbuf, res);
        intbuf = null;
        break;

      case 2008 : // glellipse
        if (null==canvas) {
          errcnt=1;
          break;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(andbrushrgb);
        for (i=0; i<4; i++)tarc[i] = buf[p + 2 + i];
        for (i=4; i<8; i++)tarc[i] = 0;
        androidarcisi(tarc);
        rectf = new RectF(tarc[0],tarc[1],tarc[2],tarc[3]);
        canvas.drawOval ( rectf,  paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(andpenrgb);
        canvas.drawOval ( rectf,  paint);
        rectf = null;
        break;

      case 2093:		// glfill
        if (null==canvas) {
          errcnt=1;
          break;
        }
        tpaint=new Paint();
        if (1==RGBSEQ)
          tpaint.setARGB(buf[p+5], buf[p+4], buf[p+3], buf[p+2]);
        else
          tpaint.setARGB(buf[p+5], buf[p+2], buf[p+3], buf[p+4]);
        tpaint.setStyle(Paint.Style.FILL);
        canvas.drawRect (0, 0, view.getWidth(), view.getWidth(), tpaint);
        tpaint=null;
        break;

      case 2012:		// glfont
        face = Util.int2String(buf, p+2, cnt-2);
        errcnt=glfont0(face);
        break;

      case 2312:		// glfont2
        int size10 = buf[ p + 2 ];
        int bold = 1 & buf[ p + 3 ];
        int italic = 2 & buf[ p + 3 ];
        andunderline = 4 & buf[ p + 3 ];
        int strikeout = 8 & buf[ p + 3 ];
        int degree10 =  buf[ p + 4 ];
        andfontangle = degree10;

        face = Util.int2String(buf, p+5, cnt-5);
        fnt=new Font(face,size10, bold, italic, strikeout, andunderline, andfontangle);
        if (fnt.error) {
          errcnt=1;
        } else {
          font=fnt;
          // also set glfontextent
          FontExtent=new Font(face,size10, bold, italic, strikeout, andunderline, andfontangle);
        }
        fnt=null;
        break;

      case 2342:		//glfontangle
        andfontangle = buf[ p + 2 ];
        break;

      case 2015 :    // gllines
        if (null==canvas) {
          errcnt=1;
          break;
        }
        paint.setColor(andpenrgb);
        paint.setStyle(Paint.Style.STROKE);
        c = (cnt - 2) / 2;
        if (0 == c) break;
        {
          path.reset();
          path.moveTo(buf[p+2],buf[p+3]);
          for (i = 0; i < c - 1; i++) path.lineTo(buf[ p + 2 + 2 * (1 + i)], buf[ p + 2 + 1 + 2 * (1 + i)]);
          canvas.drawPath(path, paint);
          path.reset();
        }
        break;

      case 2094:		// glfontextent
        textstring = Util.int2String(buf, p+2, cnt-2);
        if (0==textstring.length())
          FontExtent=null;
        else {
          fnt = new Font(textstring);
          if (fnt.error)
            errcnt=1;
          else
            FontExtent=fnt;
          fnt=null;
        }
        break;

      case 2020 :    // glpaint
        if (view.child.type.equals("isigraph")) {
          if (null==canvas)
            view.child.widget.invalidate();
        } else if (view.child.type.equals("isidraw")) {
          view.child.widget.invalidate();
        }
        break;

      case 2021 :    // glpaintx
        if (view.child.type.equals("isigraph")) {
          if (null==canvas)
            view.child.widget.invalidate();
        } else if (view.child.type.equals("isidraw")) {
          view.child.widget.draw(canvas);
        }
        break;

      case 2022 :    // glpen
        andpenrgb = andrgb;
        paint.setStrokeWidth(Math.max(1.3f,(float)buf[p+2]));
        break;

      case 2023 :  // glpie
        if (null==canvas) {
          errcnt=1;
          break;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(andbrushrgb);
        for (i=0; i<8; i++)tarc[i] = buf[p + 2 + i];
        androidarcisi(tarc);
        rectf = new RectF(tarc[0],tarc[1],tarc[2],tarc[3]);
        canvas.drawArc ( rectf, tarc[4], tarc[5], true, paint);
        rectf = null;
        break;

      case 2024 :    // glpixel
        if (null==canvas) {
          errcnt=1;
          break;
        }
        paint.setColor(andrgb);
        c = (cnt - 2) / 2;
        if (0 == c) break;
        {
          float[] pts = new float[cnt-2];
          for (i = 0; i < cnt - 2; i++) pts[i] = (float)buf[p + 2 + i];
          canvas.drawPoints(pts, paint);
        }
        break;

      case 2076 :  // glpixels
        if (null==canvas) {
          errcnt=1;
          break;
        }
        a = buf[p + 2];
        b = buf[p + 3];
        w = Math.abs( buf[p + 4] );
        h = buf[p + 5];
        if ((cnt -6) == w*h) {
          int[] colors = new int[w*h];
          System.arraycopy(buf, p + 2 + 4, colors, 0, w*h);
          if (0==RGBSEQ) fliprgb (colors, w*h);
          canvas.drawBitmap(colors, 0, w, a, b, w, h, false, paint);
        }
        break;

      case 2029 :    // glpolygon
        if (null==canvas) {
          errcnt=1;
          break;
        }
        c = (cnt - 2) / 2;
        if (0 == c) break;
        {
          path.reset();
          path.moveTo(buf[p+2], buf[p+3]);
          for (i=0; i<c-1; i++) path.lineTo(buf[p+2+2*(1+i)], buf[p+2+1+2*(1+i)]);
          path.close();
          if (0 == andbrushnull) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(andbrushrgb);
            canvas.drawPath(path, paint);
            path.reset();
            path.moveTo(buf[p+2], buf[p+3]);
            for (i=0; i<c-1; i++) path.lineTo(buf[p+2+2*(1+i)], buf[p+2+1+2*(1+i)]);
            path.close();
          }
          paint.setStyle(Paint.Style.STROKE);
          paint.setColor(andpenrgb);
          canvas.drawPath(path, paint);
          path.reset();
        }
        break;

      case 2057 :    // glqextent
        textstring = Util.int2String(buf, p+2, cnt-2);
        int[] wh = qextent(textstring);
        result=new int[] {wh[0], wh[1],};
        errcnt=-1;
        break;

      case 2083 :    // glqextentw
        textstring = Util.int2String(buf, p+2, cnt-2);
        int[] ws = qextentw(textstring.split("\n"));
        if (0<ws.length) {
          result=new int[ws.length];
          for (int j=0; j<ws.length-1; j++)
            result[j]=ws[j];
        } else {
          result=new int[0];
        }
        errcnt=-1;
        break;

      case 2060 :    // glqhandles
        result=new int[] {view.getId(), 0, 0,};
        errcnt=-1;
        break;

      case 2077 :    // glqpixels
        a=buf[p+2];
        b=buf[p+3];
        w=buf[p+4];
        h=buf[p+5];
        result=new int[w*h];
// only works for isidraw; return all zero for isigraph
        if (view.child.type.equals("isidraw")) {
          view.child.bitmap.getPixels(result, 0, w, a, b, w, h);
        }
        if (0==RGBSEQ) fliprgb (result, w*h);
        errcnt=-1;
        break;

      case 2058 :    // glqtextmetrics
// Remember, Y values increase going down, so those values will be positive, and
// values that measure distances going up will be negative.
//
// public float ascent  The recommended distance above the baseline for singled spaced text.
// public float bottom  The maximum distance below the baseline for the lowest glyph in the font at a given text size.
// public float descent The recommended distance below the baseline for singled spaced text.
// public float leading The recommended additional space to add between lines of text.
// public float top     The maximum distance above the baseline for the tallest glyph in the font at a given text size.

        Paint.FontMetrics metrics=paint.getFontMetrics();
        float asc=-metrics.ascent;
        float bottom=metrics.bottom;
        float dsc=metrics.descent;
        float leading=-metrics.leading;
        float top=-metrics.top;
        float cw=qextent("8")[0];
        float cw1=qextent("M")[0];
        result=new int[] {(int)(top+bottom),(int)asc,(int)dsc,0,(int)leading,(int)cw,(int)cw1};
        errcnt=-1;
        break;

      case 2095 :    // glqtype
        result=new int[] {view.child.type.equals("isidraw")?1:0};
        errcnt=-1;
        break;

      case 2059 :    // glqwh
        result=new int[] {view.getWidth(),view.getHeight()};
        Log.d(JConsoleApp.LogTag,"glqwh : "+view.getWidth()+" "+view.getHeight());
        errcnt=-1;
        break;

      case 2031 :    // glrect
        if (null==canvas) {
          errcnt=1;
          break;
        }
        if (0 == andbrushnull) {
          i =  2;
          while (i<cnt ) {
            paint.setColor(andbrushrgb);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect (buf[p+i], buf[p+i+1], buf[p+i]+buf[p+i+2], buf[p+i+1]+buf[p+i+3], paint);
            paint.setColor(andpenrgb);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect (buf[p+i], buf[p+i+1], buf[p+i]+buf[p+i+2], buf[p+i+1]+buf[p+i+3], paint);
            i =  i+4;
          }
        } else {
          i =  2;
          paint.setColor(andpenrgb);
          paint.setStyle(Paint.Style.STROKE);
          while (i<cnt ) {
            canvas.drawRect (buf[p+i], buf[p+i+1], buf[p+i]+buf[p+i+2], buf[p+i+1]+buf[p+i+3], paint);
            i =  i+4;
          }
        }
        break;

      case 2032 :    // glrgb
        if (1==RGBSEQ)
          andrgb = Color.argb (255, buf[p+4], buf[p+3], buf[p+2]);
        else
          andrgb = Color.argb (255, buf[p+2], buf[p+3], buf[p+4]);
        break;

      case 2343 :    // glrgba
        if (1==RGBSEQ)
          andrgb = Color.argb (buf[p+5], buf[p+4], buf[p+3], buf[p+2]);
        else
          andrgb = Color.argb (buf[p+5], buf[p+2], buf[p+3], buf[p+4]);
        break;

      case 2038 :    // gltext
        if (null==canvas) {
          errcnt=1;
          break;
        }
        textstring = Util.int2String(buf, p+2, cnt-2);
        paint.setColor(andtextrgb);
        paint.setStyle(Paint.Style.FILL);
        if (0!=andfontangle) {
          canvas.save();
          canvas.rotate(-andfontangle/10f, andtextx, andtexty);
        }
        paint.getTextBounds(textstring,0,textstring.length(),rect);
        canvas.drawText(textstring, andtextx-rect.left, andtexty-rect.top, paint);
        if (0!=andfontangle)
          canvas.restore();
        break;

      case 2040 :    // gltextcolor
        andtextrgb = andrgb;
        break;

      case 2056 :    // gltextxy
        andtextx = buf[p+2];
        andtexty = buf[p+3];
        break;

      case 2045 :    // glwindoworg
        if (null==canvas) {
          errcnt=1;
          break;
        }
        andorgx += buf[p+2];
        andorgy += buf[p+3];
        canvas.translate(buf[p+2],buf[p+3]);
        break;

      default :
        Log.d(JConsoleApp.LogTag, "Glcmds: cmd not implemented " + Integer.toString(cmd));
        errcnt=1;
      }
      p =  p + cnt;
    }

    if (0>errcnt)
      res[0]=result;
    if (0<errcnt) Log.d(JConsoleApp.LogTag, "Glcmds err: " + Integer.toString(errcnt));
    return errcnt;
  }

}