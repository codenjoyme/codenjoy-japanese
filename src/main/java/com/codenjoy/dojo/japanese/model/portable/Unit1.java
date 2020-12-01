package com.codenjoy.dojo.japanese.model.portable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.codenjoy.dojo.japanese.model.portable.Unit2.*;

class Unit1 {

    private int wsMaximized = 0;
    private int clWhite = 0;
    private int clBlack = 1;
    private int clSilver = 2;
    private int clLtGray = 3;
    private int clDkGray = 5;
    private int wdCellAlignVerticalCenter = 6;
    private int wdBorderRight = 6;
    private int wdBorderBottom = 6;
    private int clRed = 4;
    private int pf4bit = 0;
    private String fsBold = "bold";
    private String ssAlt = "alt";
    private String ssShift = "shift";
    private String ssCtrl = "control";
    private String ssLeft = "left";
    private String ssRight = "right";
    private String ssMiddle = "middle";
    private String ssDouble = "double";
    private TShiftState PredButt;

    static class TApplication {
        public String Title;
        public String ExeName;

        public void ProcessMessages() {

        }
    }

    private TApplication Application = new TApplication();

    static class TXYData {
        public int[][] arr = new int[MaxLen][MaxLen]; // TODO array 1..MaxLen, 1..MaxLen
    }
    static class TXYPustot {
        public boolean[][] arr = new boolean[MaxLen][MaxLen]; // TODO array 1..MaxLen, 1..MaxLen
    }
    static class TXYRjad {
        public int[][] arr = new int[MaxLen][MaxLen]; // TODO array 1..MaxLen, 1..MaxLen
    }
    static class TFinish {
        public boolean[] arr = new boolean[MaxLen]; // TODO  array 1..MaxLen
    }
    static class TXYCountRjad {
        public int[] arr = new int[MaxLen]; // TODO  array 1..MaxLen
    }
    static class TXYVer {
        public double[][][] arr = new double[MaxLen][MaxLen][2]; // TODO [1..MaxLen, 1..MaxLen, 1..2] of Real;
    }

    static class TBordersItem {

        public int LineStyle;
    }
    static class TItem {

        public int Height;
        public int Width;
        public int BordersOutsideLineStyle;
        public int  CellsVerticalAlignment;

        public TBordersItem BordersItem(int wdBorderRight) {
            return new TBordersItem();
        }
    }
    enum TUpDownDirection {
        updUp, updDown;
    }

    static class TIniFile {

        public TIniFile(String s) {
        }

        public String ReadString(String tstr, String s, String s1) {
            return "";
        }

        public void WriteString(String tstr, String s, String s1) {
        }

        public void Close() {
        }
    }
    static class TCell {

        public int ShadingBackgroundPatternColor;
    }
    static class WordXPRange {

        public int BordersOutsideLineStyle;

        public void ConvertToTable(char c, int i, int i1) {
        }

        public TItem Item(int x) {
            return new TItem();
        }

        public TCell Cell(int y, int x) {
            return new TCell();
        }
    }

    static class WordXPRow {

        public int RangeFontSize;
        public void RangeInsertParagraphAfter() {

        }

        public void ConvertToText(OleVariant ov1, OleVariant ov11) {

        }
    }

    static class Variant {

    }

    static class OleVariant {

        public OleVariant(String s) {
        }
    }

    static class TAllData {
        public TXYVer Ver;
        public TFinish FinX, FinY;
        public TFinish ChX, ChY;
        public TXYData Data;
        public TFinish tChX, tChY;
        public TXYPustot NoSet;
    }

    static class PAllData {
        TAllData data;
    }

    static class TPredpl {
        public TPoint SetTo;
        public boolean SetDot;
        public boolean B;
    }

    static class TCurrPt { // текущий ряд (для редактирования)
        public TPoint pt; // координаты
        public boolean xy; // ряд / столбец
    }

    static class TEdit {
        public boolean Enabled;
        public String Text;

        public void SetFocus() {
        }

        public void SelectAll() {

        }
    }

    static class TUpDown {
        public int Max;
        public int Min;
        public int Position;
        public boolean Enabled;
    }

    static class TPaintBox {
        public int Height;
        public int Width;
        public Unit1.Canvas Canvas;
    }

    static class TButton {
        public boolean Enabled;
        public int Tag;
        public String Caption;

        public void Click() {

        }
    }

    static class TCheckBox {
        public boolean Checked;
        public boolean Enabled;
        public String Caption;
    }

    static class TOpenDialog {
        public String InitialDir;
        public String FileName;
        public int FilterIndex;

        public boolean Execute() {
            return true;
        }
    }

    static class TSaveDialog {
        public String InitialDir;
        public int FilterIndex;
        public String FileName;

        public boolean Execute() {
            return false;
        }
    }

    static class TSavePictureDialog {
        public String InitialDir;
        public String FileName;

        public boolean Execute() {
            return false;
        }
    }

    static class TWordApplication {
        public boolean Visible;

        public void Connect() {
        }

        public void Disconnect() {

        }
    }

    static class TRange {

        public void InsertParagraphAfter() {

        }
    }
    static class TRows {

        public WordXPRange Columns;
        public WordXPRange Rows;
        public int RangeParagraphsFormatSpaceBefore;
        public int RangeParagraphsFormatSpaceAfter;
        public int RangeParagraphsFormatFirstLineIndent;

        public WordXPRow Rows_Get_First() {
            return null;
        }
    }
    static class TWordDocument {
        public TRange Range;
        public int ParagraphsLastRangeFontSize;
        public String ParagraphsLastRangeText;

        public WordXPRange Content() {
            return new WordXPRange();
        }

        public TRows Tables_Item(int i) {
            return new TRows();
        }

        public void Activate() {
        }
    }

    static class TPanel {
        public int Height;
        public int Width;
        public int Top;
        public int Left;
    }

    static class TLabel {
        public String Caption;
    }

    static class TGroupBox {
        public int Top;
        public int Height;
    }

    static class TLines {

    }

    static class TextFile {

    }

    static class TMemo {
        public TLines Lines;

        public void Clear() {
        }
    }

    static class TVertScrollBar {
        public boolean Visible;
    }

    static class THorzScrollBar {
        public boolean Visible;
    }

    static class Form1 {
        public Constraints Constraints;
        public int Width;
        public int Height;
        public int WindowState;
        public TVertScrollBar VertScrollBar;
        public THorzScrollBar HorzScrollBar;
        public int Left;
        public int Top;
        public String Caption;
    }

    static class Constraints {
        public int MinHeight;
        public int MinWidth;
    }

    static class Screen {
        public int Width;
        public int Height;
    }

    Screen Screen = new Screen();
    Form1 Form1 = new Form1();
    TEdit edCountX = new TEdit();
    TUpDown udCountX = new TUpDown();
    TPaintBox pb = new TPaintBox();
    TButton btCalc = new TButton();
    TCheckBox cbMode = new TCheckBox();
    TEdit edCountY = new TEdit();
    TUpDown udCountY = new TUpDown();
    TButton btSave = new TButton();
    TButton btLoad = new TButton();
    TOpenDialog od = new TOpenDialog();
    TSaveDialog sd = new TSaveDialog();
    TButton btClear = new TButton();
    TEdit edInput = new TEdit();
    TCheckBox cbRjad = new TCheckBox();
    TButton btSaveBitmap = new TButton();
    TSavePictureDialog spd = new TSavePictureDialog();
    TButton btToWord = new TButton();
    TWordApplication WordApplication1 = new TWordApplication();
    TWordDocument WordDocument1 = new TWordDocument();
    TPanel Panel1 = new TPanel();
    TLabel Label1 = new TLabel();
    TCheckBox cbVerEnable = new TCheckBox();
    TCheckBox cbLoadNaklad = new TCheckBox();
    TGroupBox gbInfo = new TGroupBox();
    TLabel Label2 = new TLabel();
    TLabel Label3 = new TLabel();
    TLabel Label4 = new TLabel();
    TLabel Label5 = new TLabel();
    TMemo Memo1 = new TMemo();

    public static class TShiftState {
        public void remove(String[] strings) {
        }

        public void set(String ssLeft) {
        }
    }

    public static class TMouseButton {
    }

    public static class TPen {
        public int Color;
    }

    public static class TFont {
        public String Name;
        public String[] Style;
        public int Size;
    }

    public static class Canvas {
        public TPen pen;
        public TPen Brush;
        public TPen Pen;
        public TFont Font;

        public void Rectangle(int i, int i1, int width, int height) {
        }

        public void Draw(int i, int i1, TBitMap bmpSmall) {
        }

        public int TextWidth(String tstr) {
            return 1;
        }

        public int TextHeight(String tstr) {
            return 1;
        }

        public void TextOut(int i, int i1, String tstr) {
        }

        public void Ellipse(int i, int i1, int i2, int i3) {
        }
    }

    public static class TBitMap {
        public int Width;
        public int Height;
        public Canvas Canvas;
        public Object PixelFormat;

        public void SaveToFile(String fileName) {
        }
    }
    public Unit2 Unit2 = new Unit2();
    public Date t; // TODO TdateTime // тут хранится время начала разгадывания кроссворда, с помощью нее вычисляется время расчета
    public TPoint PredCoord;
    public TShiftState redButt;
    boolean bDown; // непомню
    TBitMap Buf, bmpTop, bmpLeft, bmpPole, bmpSmall; // битмапы для подготовки изображения
    TCurrPt CurrPt;
    boolean bChangeLen, bUpDown; // флаг изменения размера кроссворда, флаг показывающий увеличился или уменшился кроссворд
    TAllData AllData1, AllData2, AllData3; // масивы данных
    PAllData pDM, pDT, pDP; // это указаьели на массивы данных
    TPredpl Predpl; //данные предположения
    int LenX, LenY; // длинна и высота кроссворда
    TXYRjad RjadX, RjadY; // тут хранятся цифры рядов
    TXYCountRjad CountRjadX, CountRjadY; // тут хранятся количества цифер рядов
    String LoadFileName;

    public static final int wid = 10;
    public static final int fs = 5;

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ClearData(boolean all) {
      for (int x = 1; x <= MaxLen; x++) {
          for (int y = 1; y <= MaxLen; y++) {
              pDM.data.Data.arr[x][y] = 0;
              pDM.data.Ver.arr[x][y][1] = -1;
              pDM.data.Ver.arr[x][y][2] = -1;
          }
          if (all) {
              CountRjadX.arr[x] = 0;
              CountRjadY.arr[x] = 0;
          }
          pDM.data.FinY.arr[x] = false;
          pDM.data.FinX.arr[x] = false;
          pDM.data.ChY.arr[x] = true;
          pDM.data.ChX.arr[x] = true;
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void GetRjadX()
  {
      int a;
      for (int y = 1; y <= LenY; y++) {
          a = 0;
          CountRjadX.arr[y] = 1;
          for (int x = 1; x <= LenX; x++) {
              if (pDM.data.Data.arr[x][y] == 1){
                  a++;
              } else{
                  if (a != 0){
                      RjadX.arr[CountRjadX.arr[y]][y] = a;
                      a = 0;
                      CountRjadX.arr[y] = CountRjadX.arr[y] + 1;
                  }
              }
          }
          if (a != 0) {
              RjadX.arr[CountRjadX.arr[y]][y] = a;
              CountRjadX.arr[y] = CountRjadX.arr[y] + 1;
          }
          CountRjadX.arr[y] = CountRjadX.arr[y] - 1;
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void GetRjadY(){
      int a;
      for (int x = 1; x <= LenX; x++) {
          a = 0; CountRjadY.arr[x] = 1;
          for (int y = 1; y <= LenY; y++) {
              if (pDM.data.Data.arr[x][y] == 1) {
                  a++;
              } else {
                  if (a != 0) {
                      RjadY.arr[x][CountRjadY.arr[x]] = a;
                      a = 0;
                      CountRjadY.arr[x] = CountRjadY.arr[x] + 1;
                  }
              }
          }
          if (a != 0) {
              RjadY.arr[x][CountRjadY.arr[x]] = a;
              CountRjadY.arr[x] = CountRjadY.arr[x] + 1;
          }
          CountRjadY.arr[x] = CountRjadY.arr[x] - 1;
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public int GetMaxCountRjadY(){
      int Result = 0;
      for (int x = 1; x <= LenX; x++) {
          if (Result < CountRjadY.arr[x]) {
              Result = CountRjadY.arr[x];
          }
      }
      return Result;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public int GetMaxCountRjadX(){
      int Result = 0;
      for (int y = 1; y <= LenY; y++) {
          if (Result < CountRjadX.arr[y]) {
              Result = CountRjadX.arr[y];
          }
      }
      return Result;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void Draw(TPoint pt){
      final int[] w = new int[1];
      final int[] h = new int[1];
      final int[] a = new int[1];
      final boolean[] b = new boolean[1];

      class Inner {
          public void ResPanel(){
              h[0] = pb.Height + 2;
              w[0] = pb.Width + 2;
              if (Panel1.Height != h[0]) {
                  Panel1.Height = h[0];
              }
              if (Panel1.Width != w[0]) {
                  Panel1.Width = w[0];
              }
          }

          public void ResForm(){
              h[0] = pb.Height + Panel1.Top + 30 + 15;
              a[0] = gbInfo.Top + gbInfo.Height + 32;
              if (h[0] < a[0]) h[0] = a[0];
              w[0] = pb.Width + Panel1.Left + 11 + 15;
              Form1.Constraints.MinHeight = 0;
              Form1.Constraints.MinWidth = 0;
              if (w[0] < (Screen.Width - 20)) {
                  if (b[0]) Form1.Width = w[0];
                  Form1.Constraints.MinWidth = w[0];
              }
              if (h[0] < (Screen.Height - 100)) {
                  if (b[0]) {
                      Form1.Height = h[0];
                  }
                  Form1.Constraints.MinHeight = h[0];
              }
          }
      }
      Inner inner = new Inner();

      if (!cbRjad.Checked) DrawSmall();
      if (pt.x != -1) {
          DrawPole(pt);
          DrawLeft(pt.y);
          DrawTop(pt.x);
      }
      Buf.Width = bmpLeft.Width + bmpPole.Width;
      Buf.Height = bmpTop.Height + bmpPole.Height;
      Buf.Canvas.Rectangle(0, 0, Buf.Width, Buf.Height);
      Buf.Canvas.Draw(0,             bmpTop.Height, bmpLeft);
      Buf.Canvas.Draw(bmpLeft.Width, 0,             bmpTop);
      Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
      if (!cbRjad.Checked)
          Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) / 2,
                               (bmpTop.Height - bmpSmall.Height) / 2, bmpSmall);
      pb.Height = Buf.Height;
      pb.Width = Buf.Width;
      pbPaint(this);

      b[0] = (Form1.WindowState != wsMaximized);

  //    if (!bChangeLen) return;

      if (bUpDown) {
          inner.ResForm();
          inner.ResPanel();
      } else {
          inner.ResPanel();
          inner.ResForm();
      }

  {    if (Form1.VertScrollBar.Visible) {
          Form1.Width = Form1.Width + 15;
          if (!Form1.VertScrollBar.Visible) Form1.Width = Form1.Width - 15;
      }
      if (Form1.HorzScrollBar.Visible) {
          Form1.Height = Form1.Height + 15;
          if (!Form1.HorzScrollBar.Visible) Form1.Height = Form1.Height - 15;
      }
  }
      if (b[0]) {
          Form1.Left = (Screen.Width - Form1.Width) / 2;
          Form1.Top = (Screen.Height - Form1.Height) / 2;
      }
      bChangeLen = false;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DrawLeft(int yy) {
      int tx, ty, a, d, px, w, yy1, yy2;
      String tstr;
      w =  wid - 1;
      if (cbRjad.Checked)
          {
              d = (bmpLeft.Width - 1) / wid;
              a = GetMaxCountRjadX();
              if (a != d) yy = 0;
          } else {
              a = ((LenX + 1) / 2);
          }
      bmpLeft.Width = a*wid + 1;
      bmpLeft.Height = LenY*wid + 1;
      if (yy == 0) {
          bmpLeft.Canvas.pen.Color = clWhite;
          bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
          bmpLeft.Canvas.pen.Color = clBlack;
      }
      if (yy == 0) {
          yy1 = 1;
          yy2 = LenY;
      } else {
          yy1 = yy;
          yy2 = yy;
      }
      for (int y = yy1; y <= yy2; y ++) {
          if ((CurrPt.xy) && (CurrPt.pt.x == y)) {
              bmpLeft.Canvas.Brush.Color = clSilver;
          } else {
              bmpLeft.Canvas.Brush.Color = clWhite;
          }
          if ((y % 5) == 0) {
              d = 0;
          } else {
              d = 1;
          }
          for (int x = 1; x <= a; x++) {
              bmpLeft.Canvas.Rectangle((x - 1) * wid, (y - 1) * wid, x * wid + 1, y * wid + d);
          }
          for (int x = 1; x <= CountRjadX.arr[y]; x++) {
              px = a - CountRjadX.arr[y] + x;
              tx = (px - 1)*wid; ty = (y - 1)*wid;
              bmpLeft.Canvas.Rectangle(tx, ty, px*wid + 1, y*wid + d);
              tstr = Integer.toString(RjadX.arr[x][y]);
              bmpLeft.Canvas.TextOut(tx + (wid - bmpLeft.Canvas.TextWidth(tstr) + 1) / 2,
                                     ty + (wid - bmpLeft.Canvas.TextHeight(tstr)) / 2, tstr);
          }
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DrawTop(int xx) {
      int tx, ty, a, d, py, xx1, xx2;
      String tstr;
      if (cbRjad.Checked) {
          d = (bmpTop.Height - 1) / wid;
          a = GetMaxCountRjadY();
          if (a != d) xx = 0;
      } else {
          a = ((LenY + 1) / 2);
      }
      bmpTop.Width = LenX*wid + 1;
      bmpTop.Height = a*wid + 1;
      if (xx == 0) {
          bmpTop.Canvas.pen.Color = clWhite;
          bmpTop.Canvas.Rectangle(0, 0, bmpTop.Width, bmpTop.Height);
          bmpTop.Canvas.pen.Color = clBlack;
      }
      if (xx == 0) {
          xx1 = 1;
          xx2 = LenX;
      } else {
          xx1 = xx;
          xx2 = xx;
      }
      for (int x = xx1; x <=xx2; x++) {
          if ((!CurrPt.xy) && (CurrPt.pt.x == x)) {
              bmpTop.Canvas.Brush.Color = clSilver;
          } else {
              bmpTop.Canvas.Brush.Color = clWhite;
          }
          if ((x % 5) == 0) {
              d = 0;
          } else {
              d = 1;
          }
          for (int y = 1; y <= a; y++) {
              bmpTop.Canvas.Rectangle((x - 1) * wid, (y - 1) * wid, x * wid + d, y * wid + 1);
          }
          for (int y = 1; y <= CountRjadY.arr[x]; y++) {
              py = a - CountRjadY.arr[x] + y;
              tx = (x - 1)*wid; ty = (py - 1)*wid;
              bmpTop.Canvas.Rectangle(tx, ty, x*wid + d, py*wid + 1);
              tstr = Integer.toString(RjadY.arr[x][y]);
              bmpTop.Canvas.TextOut(tx + (wid - bmpTop.Canvas.TextWidth(tstr) + 1) / 2,
                                    ty + (wid - bmpTop.Canvas.TextHeight(tstr)) / 2, tstr);
          }
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void RefreshPole(){
      DrawPole(new TPoint(0, 0));
      if (!cbRjad.Checked) {
          DrawSmall();
      }
      Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
      if (!cbRjad.Checked) {
          Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) / 2,
                  (bmpTop.Height - bmpSmall.Height) / 2, bmpSmall);
      }
      pbPaint(this);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DrawPole(TPoint pt) {
      int d1, d2, tx1, ty1, tx2, ty2;
      TPoint pt1 = new TPoint(0, 0);
      TPoint pt2 = new TPoint(0, 0);
      bmpPole.Width = LenX*wid + 1;
      bmpPole.Height = LenY*wid + 1;

      if ((pt.x == 0) && (pt.y == 0)) {
          bmpPole.Canvas.Pen.Color = clWhite;
          bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
          bmpPole.Canvas.Pen.Color = clBlack;
      }
      if (pt.x == 0) {
          pt1.x = 1;
          pt2.x = LenX;
      } else {
          pt1.x = pt.x;
          pt2.x = pt.x;
      }
      if (pt.y == 0) {
          pt1.y = 1;
          pt2.y = LenY;
      } else {
          pt1.y = pt.y;
          pt2.y = pt.y;
      }
      for (int x = pt1.x; x <= pt2.x; x++) {
          for (int y = pt1.y; x <= pt2.y; x++) {
              if ((x % 5) == 0) {
                  d1 = 0;
              } else {
                  d1 = 1;
              }
              if ((y % 5) == 0) {
                  d2 = 0;
              } else {
                  d2 = 1;
              }
              tx1 = (x - 1) * wid;
              ty1 = (y - 1) * wid;
              tx2 = x * wid;
              ty2 = y * wid;
              switch (pDM.data.Data.arr[x][y]){
                  case 0: {
                      bmpPole.Canvas.Brush.Color = clWhite;
                      bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                  } break;
                  case 1: {
                      bmpPole.Canvas.Brush.Color = clLtGray;
                      bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                      bmpPole.Canvas.Brush.Color = clBlack;
                      bmpPole.Canvas.Ellipse(tx1 + 2, ty1 + 2, tx2 - 2, ty2 - 2);
                  } break;
                  case 2: {
                      bmpPole.Canvas.Brush.Color = clLtGray;
                      bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                  } break;
                  case 3: {
                      bmpPole.Canvas.Brush.Color = clRed;
                      bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                  } break;
              }
          }
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DrawSmall(){
      int w;
      w =  wid / 3;
      bmpSmall.Width = LenX*w;
      bmpSmall.Height = LenY*w;
      bmpSmall.Canvas.Rectangle(0, 0, bmpSmall.Width, bmpSmall.Height);
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++) {
              switch (pDM.data.Data.arr[x][y]) {
                  case 0:
                  case 2: {
                      bmpSmall.Canvas.Pen.Color = clWhite;
                      bmpSmall.Canvas.Brush.Color = clWhite;
                  }
                  break;
                  case 1: {
                      bmpSmall.Canvas.Pen.Color = clBlack;
                      bmpSmall.Canvas.Brush.Color = clBlack;
                  }
                  break;
              }
              bmpSmall.Canvas.Rectangle((x - 1) * w, (y - 1) * w, x * w + 1, y * w + 1);
          }
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public String ExtractFileDir(String s) {
    return s;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void FormCreate(Object Sender) {
      pDM.data = AllData1;
      pDT.data = AllData2;
      pDP.data = AllData3;
      Application.Title = Form1.Caption;
      udCountX.Max = MaxLen;
      udCountY.Max = MaxLen;
      PredCoord = new TPoint(-1, -1);
      PredButt.set(ssLeft);
      CurrPt.xy = true;
      CurrPt.pt = new TPoint(1, 1);
  //    edInput.SetFocus;
      bChangeLen = true;
      bUpDown = false;
      LenX = udCountX.Position;
      LenY = udCountY.Position;
      bDown = false;
      Predpl.SetDot = false;
      Predpl.B = false;

      ClearData(true);

      SetInfo(0, true, false, true, 0);

      od.InitialDir = ExtractFileDir(Application.ExeName);
      sd.InitialDir = od.InitialDir;
      spd.InitialDir = od.InitialDir;

      Buf = new TBitMap();
      Buf.PixelFormat = pf4bit;
      Buf.Width = 10;
      Buf.Height = 10;

      bmpLeft = new TBitMap();
      bmpLeft.PixelFormat = pf4bit;
      bmpLeft.Width = 10;
      bmpLeft.Height = 10;
      bmpLeft.Canvas.Pen.Color = clBlack;
      bmpLeft.Canvas.Brush.Color = clWhite;
      bmpLeft.Canvas.Font.Name = "Times New Roman";
      bmpLeft.Canvas.Font.Style = new String[]{fsBold};
      bmpLeft.Canvas.Font.Size = fs;

      bmpTop = new TBitMap();
      bmpTop.PixelFormat = pf4bit;
      bmpTop.Width = 10;
      bmpTop.Height = 10;
      bmpTop.Canvas.Pen.Color = bmpLeft.Canvas.Pen.Color;
      bmpTop.Canvas.Brush.Color = bmpLeft.Canvas.Brush.Color;
      bmpTop.Canvas.Font.Name = bmpLeft.Canvas.Font.Name;
      bmpTop.Canvas.Font.Style = bmpLeft.Canvas.Font.Style;
      bmpTop.Canvas.Font.Size = bmpLeft.Canvas.Font.Size;

      bmpPole = new TBitMap();
      bmpPole.PixelFormat = pf4bit;
      bmpPole.Width = 10;
      bmpPole.Height = 10;
      bmpPole.Canvas.Pen.Color = clBlack;
      bmpPole.Canvas.Brush.Color = clWhite;

      bmpSmall = new TBitMap();
      bmpSmall.PixelFormat = pf4bit;
      bmpSmall.Width = 10;
      bmpSmall.Height = 10;
      bmpSmall.Canvas.Pen.Color = clBlack;
      bmpSmall.Canvas.Brush.Color = clWhite;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void FormDestroy() {
//      Buf.Free;
//      bmpLeft.Free;
//      bmpTop.Free;
//      bmpPole.Free;
//      bmpSmall.Free;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ShowMessage(String message) {

  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void pbMouseDown(TMouseButton Button, TShiftState Shift, int X, int Y){
      int j, k;
      Shift.remove(new String[]{ ssDouble }); // когда 2 раза нажимаеш возникает и это сообщение, а оно портачит
      if (edInput.Enabled) edInput.SetFocus(); // фокус едиту! :)
      if ((X < bmpLeft.Width) && (Y < bmpTop.Height)) return; // если кликаем в области SmallBmp то выходим
      if (X < bmpLeft.Width) { // тут находится bmpLeft
  //        if (cbRjad.Checked) return; // это пока не проработал...
          Y = Y - bmpTop.Height; // получаем координаты начала bmpLeft
          X = X;
          Y = (Y / wid) + 1; // теперь номер ячейки
          X = (X / wid);

          ChangeActive(new TPoint(Y, 0), true); // записываем номер ячейки

          if (Shift.equals(new String[]{ssAlt, ssLeft})) { // если нужно расчитать этот ряд
              PrepRjadX(pDM, Y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка ряда
              Unit2.glLen = LenX; // длинна
              if (!Unit2.Calculate()) { // расчет - если не получился ...
                  ShowMessage("Ошибка в кроссворде (строка " + Integer.toString(Y) + ").");
                  RefreshPole(); // обновляем поле
                  return; // выходим
              }
              for (int x = 1; x <= LenX; x++) {// тут обновляем ряд
                  pDM.data.Data.arr[x][Y] = Unit2.glData.arr[x];
                  pDM.data.Ver.arr[x][Y][1] = Unit2.glVer.arr[x];
              }
  //            GetFin;
              RefreshPole(); // и выводим его на екран
              return; // выходим
          }
          // пересчет для координат для рядов
          if (cbRjad.Checked) {
              X = X + 1 - (GetMaxCountRjadX() - CountRjadX.arr[Y]);
          } else {
              X = X - (((LenX + 1) / 2) - CountRjadX.arr[Y]) + 1;
          }

          if (Shift.equals(new String[]{ssCtrl, ssLeft})) { // сдвиг рядов чисел
              for (int ty = (Y + 1); ty <= LenY; ty++) {
                  CountRjadX.arr[ty - 1] = CountRjadX.arr[ty];
                  for (int tx = 1; tx <= CountRjadX.arr[ty]; tx++) {
                      RjadX.arr[tx][ty - 1] = RjadX.arr[tx][ty];
                  }
              }
              CountRjadX.arr[LenY] = 0;
          }
          if (Shift.equals(new String[]{ssCtrl, ssRight})) { // то же но в другую сторону
              for (int ty = LenY; ty >= Y; ty --) {    // а возможен глюк
                  CountRjadX.arr[ty] = CountRjadX.arr[ty - 1];
                  for (int tx = 1; tx <= CountRjadX.arr[ty]; tx++) {
                      RjadX.arr[tx][ty] = RjadX.arr[tx][ty - 1];
                  }
              }
              CountRjadX.arr[Y] = 0;
          }
          if (Shift.equals(new String[]{ssLeft})) { // добавляем еще одну точку
              // получаем сумму всех точек, включая пустые промежутки из длинной в 1
              j = 0;
              for (int tx = 1; tx <= CountRjadX.arr[Y]; tx++) {
                  j = j + RjadX.arr[tx][Y];
              }
              if (X <= 0) { // добавление новой
                  if ((j + CountRjadX.arr[Y]) > (LenX - 1)) return; // если выходим при добавлении за граници, то выходим
                  for ( int tx = CountRjadX.arr[Y]; tx >= 1; tx --) {
                      // смещаем все цифры ряда для добавления 1
                      RjadX.arr[tx + 1][Y] =RjadX.arr[tx][Y];
                  }
                  X = 1; // позиция добавления
                  CountRjadX.arr[Y] = CountRjadX.arr[Y] + 1; // длинна ряда увеличилась
                  RjadX.arr[X][Y] = 0; // пока ноль...
              }
              if ((j + CountRjadX.arr[Y]) > LenX) return; // если превышаем длинну - то выходим
              RjadX.arr[X][Y] = RjadX.arr[X][Y] + 1; // увеличиваем на 1
          }
          // тут уменьшаем на 1
          if (Shift.equals(new String[]{ssRight})) {
              if (CountRjadX.arr[Y] == 0) return; // если ряд пуст то выходим
              if (X <= 0) X = 1; // если нажали на пустую ячейку, то удалять будем первый
              RjadX.arr[X][Y] = RjadX.arr[X][Y] - 1; // удаляем
              if (RjadX.arr[X][Y] == 0) { // если там ноль получился
                  if (X != CountRjadX.arr[Y]) { // и этот ноль не в конце
                      for (int tx = X; tx <= CountRjadX.arr[Y]; tx++) { // то сдвигаем
                          RjadX.arr[tx][Y] = RjadX.arr[tx + 1][Y];
                      }
                  }
                  CountRjadX.arr[Y] = CountRjadX.arr[Y] - 1; // уменьшаем на 1 количество
              }
          }
          // тут прорисовка
          if (cbMode.Checked) {
              DataFromRjadX(Y);
              DrawPole(new TPoint(0, Y));
              DrawTop(0);
          }
          if ((Shift.equals(new String[]{ssCtrl, ssLeft}))
                  || (Shift.equals(new String[]{ssCtrl, ssRight}))) {
              DrawLeft(0);
          } else {
              DrawLeft(Y);
          }
          Draw(new TPoint(-1, -1));
          return;
      }
      // далее то же, но только с рядами в bmpTop
      if (Y < bmpTop.Height) {
          Y = Y;
          X = X - bmpLeft.Width;
          Y = (Y / wid);
          X = (X / wid) + 1;

          ChangeActive(new TPoint(X, 0), false); // записываем номер ячейки

          if (Shift.equals(new String[]{ssAlt, ssLeft})) {
              PrepRjadY(pDM, X, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
              Unit2.glLen = LenY;
              if (!Unit2.Calculate()) {
                  ShowMessage("Ошибка в кроссворде (столбец " + Integer.toString(X) + ").");
                  RefreshPole();
                  return;
              }
              for (int y = 1; y <= LenY; y++) {
                  pDM.data.Data.arr[X][y] = Unit2.glData.arr[y];
                  pDM.data.Ver.arr[X][y][2] = Unit2.glVer.arr[y];
              }
  //            GetFin();
              RefreshPole();
              return;
          }

          if (cbRjad.Checked) {
              Y = Y + 1 - (GetMaxCountRjadY() - CountRjadY.arr[X]);
          } else {
              Y = Y - (((LenY + 1) / 2) - CountRjadY.arr[X]) + 1;
          }
          if (Shift.equals(new String[]{ssCtrl, ssLeft})) {
              for (int tx = (X + 1); tx <= LenX; tx++) {
                  CountRjadY.arr[tx - 1] = CountRjadY.arr[tx];
                  for ( int ty = 1; ty <= CountRjadY.arr[tx]; ty++) {
                      RjadY.arr[tx - 1][ty] = RjadY.arr[tx][ty];
                  }
              }
              CountRjadY.arr[LenX] = 0;
          }
          if (Shift.equals(new String[]{ssCtrl, ssRight})) {
              for (int tx = LenX; tx >= X; tx--) {    // аозможен глюк
                  CountRjadY.arr[tx] = CountRjadY.arr[tx - 1];
                  for (int ty = 1; ty <= CountRjadY.arr[tx]; ty++) {
                      RjadY.arr[tx][ty] = RjadY.arr[tx - 1][ty];
                  }
              }
              CountRjadY.arr[X] = 0;
          }
          if (Shift.equals(new String[]{ssLeft})) {
              j = 0;
              for (int ty = 1; ty <= CountRjadY.arr[X]; ty++) {
                  j = j + RjadY.arr[X][ty];
              }
              if (Y <= 0) {
                  if ((j + CountRjadY.arr[X]) > (LenY - 1)) return;
                  for (int ty = CountRjadY.arr[X]; ty >= 1; ty--) {
                      RjadY.arr[X][ty + 1] = RjadY.arr[X][ty];
                  }
                  Y = 1;
                  CountRjadY.arr[X] = CountRjadY.arr[X] + 1;
                  RjadY.arr[X][Y] = 0;
              }
              if ((j + CountRjadY.arr[X]) > LenY) return;
              RjadY.arr[X][Y] = RjadY.arr[X][Y] + 1;
          }
          if (Shift.equals(new String[]{ssRight})) {
              if (CountRjadY.arr[X] == 0) return;
              if (Y <= 0) Y = 1;
              RjadY.arr[X][Y] = RjadY.arr[X][Y] - 1;
              if (RjadY.arr[X][Y] == 0) {
                  if (Y != CountRjadY.arr[X]) {
                      for (int ty = Y; ty <= CountRjadY.arr[X]; ty++) {
                          RjadY.arr[X][ty] =RjadY.arr[X][ty + 1];
                      }
                  }
                  CountRjadY.arr[X] = CountRjadY.arr[X] - 1;
              }
          }

          if (cbMode.Checked) {
              DataFromRjadY(X);
              DrawPole(new TPoint(X, 0));
              DrawLeft(0);
          }
          if ((Shift.equals(new String[]{ssCtrl, ssLeft}))
                  || (Shift.equals(new String[]{ssCtrl, ssRight}))) {
              DrawTop(0);
          } else {
              DrawTop(X);
          }
          Draw(new TPoint(-1, -1));

          return;
      }
      bDown = true;
      pbMouseMove(Shift, X, Y); // а тут если на поле попали
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DataFromRjadX(int y){
      int k, tx, j;
      for (tx = 1; tx <= LenX; tx ++) {
          pDM.data.Data.arr[tx][y] = 0;
      }
      k = 1;
      tx = 1;
      while (tx <= CountRjadX.arr[y]) {
          for (j = 1; j <= RjadX.arr[tx][y]; j++) {
              pDM.data.Data.arr[k + j - 1][y] = 1;
          }
          pDM.data.Data.arr[k + j][y] = 0; // TODO тут непонятно с операторными скобками
          k = k + j;
          tx++;
      }
      GetRjadY();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void DataFromRjadY(int x) {
      int k, j, ty;
      for (ty = 1; ty <= LenY; ty++) {
          pDM.data.Data.arr[x][ty] = 0;
      }
      k = 1;
      ty = 1;
      while (ty <= CountRjadY.arr[x]) {
          for (j = 1; j <= RjadY.arr[x][ty]; j++) {
              pDM.data.Data.arr[x][k + j - 1] =1;
          }
          pDM.data.Data.arr[x][k + j] = 0;
          k = k + j;
          ty++;
      }
      GetRjadX();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void pbPaint(Object Sender) {
      pb.Canvas.Draw(0, 0, Buf);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void edCountXChange() {
      int a;
      bChangeLen = true;
      ChangeActive(new TPoint(1, 1), true); // записываем номер ячейки
      LenX = udCountX.Position;
      LenY = udCountY.Position;
      Draw(new TPoint(0, 0));
      return;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void pbMouseMove(TShiftState Shift, int X, int Y) {
      boolean bDraw;
      Y = Y - bmpTop.Height;
      X = X - bmpLeft.Width;
      if ((X < 0) ||  (Y < 0)) {
          Label1.Caption = "";
          return;
      }
      Y = (Y / wid) + 1;
      X = (X / wid) + 1;
      if ((Y <= 0) ||  (Y > LenY) ||  (X <= 0) ||  (X > LenX)) {
          Label1.Caption = "";
          return;
      }
      Label1.Caption = Double.toString(Math.round(pDM.data.Ver.arr[X][Y][1]*100)/100) + "     " + Double.toString(Math.round(pDM.data.Ver.arr[X][Y][2]*100)/100);
      if (!bDown) return;
      bDraw = false;
      if (Shift.equals(new String[]{ssLeft})) {
          bDraw = (pDM.data.Data.arr[X][Y] != 1);
          pDM.data.Data.arr[X][Y] = 1;
      }
      if (Shift.equals(new String[]{ssRight})) {
          bDraw = (pDM.data.Data.arr[X][Y] != 0);
          pDM.data.Data.arr[X][Y] = 0;
      }
      if (Shift.equals(new String[]{ssMiddle})) {
          bDraw = (pDM.data.Data.arr[X][Y] != 2);
          pDM.data.Data.arr[X][Y] = 2;
      }
      PredCoord = new TPoint(X, Y);
      PredButt = Shift;
      if (bDraw) {
          if (cbMode.Checked) {
              GetRjadX();
              GetRjadY();
              DrawLeft(Y);
              DrawTop(X);
          }
          DrawPole(new TPoint(X, Y));
          Draw(new TPoint(-1, -1));
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void pbMouseUp(TMouseButton Button, TShiftState Shift, int X, int Y) {
      bDown = false;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void cbModeClick() {
      btCalc.Enabled = !cbMode.Checked;
      cbVerEnable.Enabled = btCalc.Enabled;
      if (cbMode.Checked) {
          cbMode.Caption = "Редактор";
      } else {
          cbMode.Caption = "Расш.";
      }
      if (cbMode.Checked) {
          GetRjadX();
          GetRjadY();
      }
      DrawLeft(0);
      DrawTop(0);
      Draw(new TPoint(-1, -1));
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public TPoint Check()
  {
      int a1, a2;
      a1 = 0;
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= CountRjadY.arr[x]; y++) {
              a1 = a1 + RjadY.arr[x][y];
          }
      }
      a2 = 0;
      for (int x = 1; x <= LenY; x++) {
          for (int y = 1; y <= CountRjadX.arr[y]; y++) {
              a2 = a2 + RjadX.arr[x][y];
          }
      }
      return new TPoint(a1, a2);  // разница рядов
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btCalcClick() {
      boolean b, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, c; // b - произошли ли изменения, b2 - была ли ошибка, b3 - , b4 - , b5 - предполагать максимальной вероятности с учетом массива NoSet, b6 - если точка с максимальной вероятностью была найдена, b7 - последний прогон для нормального отображения вероятностей, b8 - если нажали остановить, b9 - если остановка по ошибке, b11 - нудно для пропуска прогона по у если LenX больше LenY
      int h, m, s, ms; // word
      double MaxVer1, MaxVer2; // real
      double a1, a2; // real
      TPoint pt;
      PAllData pWork;
      boolean bErrT, bErrP;
      int p; // p:^byte;

      if (btCalc.Tag == 0) { // интерфейсные изменение Остановить-Расчет
          btCalc.Caption = "&Стоп      ";
          btCalc.Tag = 1;
          udCountX.Enabled = false;
          udCountY.Enabled = false;
          cbMode.Enabled = false;
          btSave.Enabled = false;
          btLoad.Enabled = false;
          cbLoadNaklad.Enabled = true;
          cbVerEnable.Enabled = false;
          btClear.Enabled = false;
          btToWord.Enabled = false;
          btSaveBitmap.Enabled = false;
          edInput.Enabled = false;
          edCountX.Enabled = false;
          edCountY.Enabled = false;
      } else {
          btCalc.Caption = "&Расчет    ";
          btCalc.Tag = 0;
          udCountX.Enabled = true;
          udCountY.Enabled = true;
          cbMode.Enabled = true;
          btSave.Enabled = true;
          btLoad.Enabled = true;
          cbLoadNaklad.Enabled = true;
          cbVerEnable.Enabled = true;
          btClear.Enabled = true;
          btToWord.Enabled = true;
          btSaveBitmap.Enabled = true;
          edCountX.Enabled = true;
          edCountY.Enabled = true;
          edInput.Enabled = true;
          edInput.SetFocus();
          RefreshPole(); // прорисовка поля
          SetInfo(0, true, false, false, 0);
          return; // сразу выходим
      }

      t = Calendar.getInstance().getTime(); //

      // проверка на совпадение рядов
      pt = Check();
      int x0 = Math.abs(pt.x - pt.y);
      if (x0 > 0) {
          ShowMessage("Ошибка! Несовпадение на " + Integer.toString(x0));
          btCalc.Click(); // остановка
          return;
      }
      //-----------------------------
      // сам рачсет
      for (int x = 1; x <= LenX; x++) {
          h = 0;
          for (int y = 1; y <= CountRjadX.arr[x]; y++) {
              h = h + RjadX.arr[x][y];
          }
          if (h < (LenX / 2)) {
              pDM.data.ChY.arr[x] = false;
          }
      }
      for (int y = 1; y <= LenY; y++) {
          h = 0;
          for (int x = 1; x < CountRjadY.arr[y]; x++) {
            h = h + RjadY.arr[x][y];

          }
          if (h < (LenY / 2)) {
              pDM.data.ChX.arr[y] = false;
          }
      }
      //----------
      for (int x = 1; x <= LenX; x++) {
          pDM.data.tChY.arr[x] = true;
          pDT.data.tChY.arr[x] = true;
          pDP.data.tChY.arr[x] = true;
      }
      for (int y = 1; y <= LenY; y++) {
          pDM.data.tChX.arr[y] = true;
          pDT.data.tChX.arr[y] = true;
          pDP.data.tChX.arr[y] = true;
      }
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++) {
              pDM.data.NoSet.arr[x][y] = false;
              pDT.data.NoSet.arr[x][y] = false;
              pDP.data.NoSet.arr[x][y] = false;
          }
      }
      b5 = false;
      b7 = false;
      b8 = false;
      b9 = false;
      // для пропуска прогона по у если в группе x и чисел меньше (значения больше) и длинна строки (группы) меньше, это все для ускорения
      b11 = (LenX > LenY); // нужно для пропуска прогона
      a1 = 0;
      for (int x = 1; x <= LenX; x++) {
          a1 = a1 + CountRjadY.arr[x];
      }
      a2 = 0;
      for (int y = 1; y <= LenY; y++) {
          a2 = a2 + CountRjadX.arr[y];
      }
      b11 = (a1/LenY > a2/LenX);
      //----------------------------------------------------------
      Predpl.B = false;
      b = false; // для b11
      bErrT = false;
      bErrP = false;
      Memo1.Clear();
      do {
          if (b && b11) b11 = false;
          b = false;
          b2 = false;
          // с каким указателем работаем
          if (Predpl.B) {
              if (Predpl.SetDot) {
                  pWork = pDT;
              } else {
                  pWork = pDP;
              }
          } else {
              pWork = pDM;
          }

          if (!(b5 || b11)) { // при поиску другой точки, или если LenX больше LenY (в начале) пропускаем этот шаг
              for (int y = 1; y <= LenY; y++) {
//                SetInfo(y, true, bPredpl, false, Predpl.SetDot);
                  Application.ProcessMessages();  // передышка
                  b8 = (btCalc.Tag == 0); // остановка
                  if (b8) break;
                  if (pWork.data.FinX.arr[y]) continue;
                  if (!pWork.data.ChX.arr[y]) continue;
                  PrepRjadX(pWork, y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка строки
                  Unit2.glLen = LenX; // длинна строки
//  if (bPredpl) {
//      if (Predpl.SetDot == 1) {
//          Memo1.Lines.Add("Ряд: " + Integer.toString(y) + " предп. т");
//      } else {
//          Memo1.Lines.Add("Ряд: " + Integer.toString(y) + " предп. п");
//      }
//  } else {
//      Memo1.Lines.Add("Ряд: " + Integer.toString(y) + " точно");
//  }
                  if (!Unit2.Calculate()) { // расчет ... если нет ни одной комбины - ошибка
                      if (!cbVerEnable.Checked) {
                          ShowMessage("Ошибка в кроссворде (строка " + Integer.toString(y) + ").");
                          b9 = true;
                          break;
                      }
                      b = false; // изменений нету
                      b2 = true; // ошибка была
                      break;
                  }
                  for (int x = 1; x < LenX; x++) {
                       pWork.data.Ver.arr[x][y][1] = Unit2.glVer.arr[x];

                       if (pWork.data.Data.arr[x][y] != Unit2.glData.arr[x]) {
                           pWork.data.Data.arr[x][y] = Unit2.glData.arr[x];
                          if (!b) {
                              b = true; // b = true;
                          }
                          if (!pWork.data.ChY.arr[x]) {
                              pWork.data.ChY.arr[x] = true; // pWork.data.ChY.arr[x] = true;
                          }
                          if (Predpl.B) {
                              if (!pWork.data.tChY.arr[x]) {
                                  pWork.data.tChY.arr[x] = true; // pWork.data.tChY.arr[x] = true;
                              }
                          }
                      }
                  }
                  pWork.data.ChX.arr[y] = false;
              }
              if (!Predpl.B) RefreshPole(); // прорисовка поля только в случае точного расчета
          }
          if (!b9) {
              b9 = GetFin(pWork); // если небыло ошибки, то если сложили все b9 = GetFin; выходим как если была бы ошибка
          }

          if ((!b2) && (!b5) && (!b8) && (!b9)) { // если была ошибка (b2) или надо найти другую точку (b5) или принудительно заканчиваем (b8) или была ошибка (b9) то пропускаем этот шаг
              for (int x = 1; x < LenX; x++) { // дальше то же только для столбцов
  //                SetInfo(x, false, bPredpl, false, Predpl.SetDot);
                  Application.ProcessMessages();
                  b8 = (btCalc.Tag == 0); // остановка
                  if (b8) break;
                  if (pWork.data.FinY.arr[x]) continue;
                  if (!pWork.data.ChY.arr[x]) continue;
                  PrepRjadY(pWork, x, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
                  Unit2.glLen = LenY;
  //if (bPredpl) {
  //    if (Predpl.SetDot == 1) {
  //        Memo1.Lines.Add("Ст.: " + Integer.toString(x) + " предп. т")
  //    } else {
  //        Memo1.Lines.Add("Ст.: " + Integer.toString(x) + " предп. п");
  //    }
  //} else {
  //    Memo1.Lines.Add("Ст.: " + Integer.toString(x) + " точно");
  //}
                  if (!Unit2.Calculate()) {
                      if (!cbVerEnable.Checked) {
                          ShowMessage("Ошибка в кроссворде (столбец " + Integer.toString(x) + ").");
                          b9 = true;
                          break;
                      }
                      b = false; // изменений нету
                      b2 = true; // ошибка была
                      break;
                  }
                  c = false;
                  for (int y = 1; y <= LenY; y++) {
                      pWork.data.Ver.arr[x][y][2] = Unit2.glVer.arr[y];

                      if (pWork.data.Data.arr[x][y] != Unit2.glData.arr[y]) {
                          pWork.data.Data.arr[x][y] = Unit2.glData.arr[y];
                          if (!b) {
                              b = true; // b = true;
                          }
                          if (!pWork.data.ChX.arr[y]) {
                              pWork.data.ChX.arr[y] = true; // pWork.data.ChX.arr[y] = true;
                          }
                          if (Predpl.B) {
                              if (!pWork.data.tChY.arr[x]) {
                                  pWork.data.tChY.arr[x] = true; // pWork.data.tChY.arr[x] = true;
                              }
                          }
                      }
                  }
                  pWork.data.ChY.arr[x] = false;
              }
              if (!Predpl.B) RefreshPole();// прорисовка поля
              if (b11) b = true; // чтобы после прогона по х пошел прогон по у
          }
          if (!b9)
              b9 = GetFin(pWork); // если небыло ошибки, то если сложили все b9 = GetFin; выходим как если была бы ошибка

          if (b7 || b8) b = false; // все конец
          if ((cbVerEnable.Checked) && (!b) && (!b7) && (!b8) && (!b9)) { // если ничего не получается решить точно (b) и включено предположение (cbVerEnable.Checked) и последнего прогона нет (b7) и принудительно незавершали (b8) и ошибки нету
              if (b11) b11 = false;

              if (Predpl.B) { // предполагали?
                  // да
                  if (Predpl.SetDot) { // запоминаем ошибки
                      bErrT = b2;
                  } else {
                      bErrP = b2;
                  }
                  if (b2) b2 = false; // была
                  if (Predpl.SetDot) { // что было
                      SavePustot(Predpl.SetTo, false); //была точка, теперь пустота
                      b = true; // произошли изменения
                  } else { // путота, значит будем определять что нам записывать
                      if (bErrT) {
                          // ошибка на точке
                          if (bErrP) {
                              // ошибка на точке и на пустоте - ошибка в кроссворде
                              ShowMessage("Ошибка в кроссворде.");
                              b9 = true;
                          } else { // ошибка на точке и нет ее на пустоте - значит пустота
                              ChangeDataArr(false);
                              RefreshPole();
                              b5 = true; // дальше предполагаем
                              b = true; // продолжаем дальше
                          }
                      } else {  // нет ошибки на точке
                          if (bErrP) { // нету на точке и есть на пустоте - значит точка
                              ChangeDataArr(true);
                              RefreshPole();
                              b5 = true; // дальше предполагаем
                              b = true; // продолжаем дальше
                          } else { // нет ни там ни там - значит неизвестно, это потом сохранять будем
                              pt = Predpl.SetTo;
                              pDM.data.NoSet.arr[pt.x][pt.y] = true;
                              pDM.data.Data.arr[pt.x][pt.y] = 0;
                              Draw(pt);
                              b5 = true; // дальше предполагаем
                              b = true; // продолжаем дальше
                          }
                      }
                      Predpl.B = false;
                  }
              } else {
                  if (b2) { // ошибка была?
                      // если была ошибка без предположений то в кросворде ошибка
                      ShowMessage("Ошибка в кроссворде.");
                      b9 = true;
                  } else { // еще не предполагали
                          MaxVer1 = 0; // пока вероятности такие
                          MaxVer2 = 0;
                          b6 = false;
                          for (int x = 1; x <= LenX; x++) {  // по всему полю
                              for (int y = 1; y <= LenY; y++) {
                                  if ((MaxVer1 <= pDM.data.Ver.arr[x][y][1])
                                          && (MaxVer2 <= pDM.data.Ver.arr[x][y][2])
                                          && (pDM.data.Ver.arr[x][y][1] < 1)
                                          && (pDM.data.Ver.arr[x][y][2] < 1)) { // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                      if (pDM.data.NoSet.arr[x][y]) continue;
                                      MaxVer1 = pDM.data.Ver.arr[x][y][1];
                                      MaxVer2 = pDM.data.Ver.arr[x][y][2];
                                      pt = new TPoint(x, y);
                                      b6 = true;
                                  }
                              }
                          }

                          b6 = b6 && ((MaxVer1 > 0) ||  (MaxVer2 > 0)); // критерий отбора
                          if (b6) { // нашли точку?
                              // да
                              if (!b5) {
                                  Predpl.B = true;
                                  SavePustot(pt, true); // сохраняемся только если искали макс вероятность без учета массива NoSet
                              }
                              pDM.data.Data.arr[pt.x][pt.y] = 3;
                              Draw(pt);
//Memo1.Lines.Add("Предп. в " + Integer.toString(pt.y) + ", " + Integer.toString(pt.x));
                              b = true; // произошли изменения
                          } else { // нет
                              if (b5) {
                                  Predpl.B = false;
                                  RefreshPole(); //
                              }
                              b = true; // изменений нету
                              for (int x = 1; x <= LenX; x++) {
                                  pDM.data.FinY.arr[x] = false;
                                  pDM.data.ChY.arr[x] = true;
                              }
                              for (int y = 1; y <= LenY; y++) {
                                  pDM.data.FinX.arr[y] = false;
                                  pDM.data.ChX.arr[y] = true;
                              }
                              b7 = true; // последний прогон для нормального отображения вероятностей
                          }
                          b5 = false;
                      }
                  }
          }
          if (b9) b = false; // все конец
      } while (!b);
      // очистка массивв флагов заполнености
      for (int x = 1; x <= LenX; x++) {
          pDM.data.ChY.arr[x] = true;
          pDM.data.FinY.arr[x] = false;
      }
      for (int y = 1; y <= LenY; y++) {
          pDM.data.ChX.arr[y] = true;
          pDM.data.FinX.arr[y] = false;
      }
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++) {
              switch (pDM.data.Data.arr[x][y]) {
                  case 1: {
                      pDM.data.Ver.arr[x][y][1] = 1;
                      pDM.data.Ver.arr[x][y][2] = 1;
                  }
                  break;
                  case 2: {
                      pDM.data.Ver.arr[x][y][1] = 0;
                      pDM.data.Ver.arr[x][y][2] = 0;
                  }
                  break;
              }
          }
      }
      if (b8) { // если нажали остановить
          if (Predpl.B) {
              pDM.data.Data.arr[Predpl.SetTo.x][Predpl.SetTo.y] = 0;
          }
          Predpl.B = false;
          RefreshPole(); // прорисовка поля
          SetInfo(0, true, false, false, 0);
      } else {
          btCalc.Click(); // иначе нажимем на кнопку
          SaveTime(); // сохраняем время, за которое решили кроссворд
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SetPredplDot(boolean bDot){
      TPoint pt;
      PAllData pWork;
      if (bDot) { // что предполагаем?
          pWork = pDT; // точку
      } else {
          pWork = pDP; // путоту
      }
      pt = Predpl.SetTo;
      if (bDot) {
          pWork.data.Data.arr[pt.x][pt.y] = 1;
          // меняем вероятности
          pWork.data.Ver.arr[pt.x][pt.y][1] = 1;
          pWork.data.Ver.arr[pt.x][pt.y][2] = 1;
      } else {
          pWork.data.Data.arr[pt.x][pt.y] = 2;
          // меняем вероятности
          pWork.data.Ver.arr[pt.x][pt.y][1] = 0;
          pWork.data.Ver.arr[pt.x][pt.y][2] = 0;
      }
      // строка и солбец, содержащие эту точку пересчитать
      pWork.data.ChX.arr[pt.y] = true;
      pWork.data.ChY.arr[pt.x] = true;
      pWork.data.FinX.arr[pt.y] = false;
      pWork.data.FinY.arr[pt.x] = false;
      Draw(pt);
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ChangeDataArr(boolean bDot)
  {
      PAllData p;
      if (bDot) {
          p = pDM;
          pDM = pDT;
          pDT = p;
      } else {
          p = pDM;
          pDM = pDP;
          pDP = p;
      }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SavePustot(TPoint pt, boolean bDot) {
      boolean b, b2;
      PAllData pWork;
      if (bDot) { // что предполагаем?
          pWork = pDT; // точку
      } else {
          pWork = pDP; // путоту
      }
      for (int x = 1; x <= LenX; x++) { // по всему полю
          for (int y = 1; y <= LenY; y++) {
              pWork.data.Data.arr[x][y] = pDM.data.Data.arr[x][y];
              pWork.data.Ver.arr[x][y][1] = pDM.data.Ver.arr[x][y][1];
              pWork.data.Ver.arr[x][y][2] = pDM.data.Ver.arr[x][y][2];
              b = (pDM.data.Data.arr[x][y] == 0); // пусто?
              if (b) {
                  // пусто
                  if (pWork.data.tChY.arr[x] && pWork.data.tChX.arr[y]) { //если производились изменения
                      pWork.data.NoSet.arr[x][y] = false; // ставить можна
                  }
              } else {
                  pWork.data.NoSet.arr[x][y] = true; // непусто - сюда ставить нельзя
              }
          }
      }
      for (int x = 1; x <= LenX; x++) {
          pWork.data.ChY.arr[x] = pDM.data.ChY.arr[x];
          pWork.data.FinY.arr[x] = pDM.data.FinY.arr[x];
          pWork.data.tChY.arr[x] = false;
      }
      for (int y = 1; y <= LenY; y++) {
          pWork.data.ChX.arr[y] = pDM.data.ChX.arr[y];
          pWork.data.FinX.arr[y] = pDM.data.FinX.arr[y];
          pWork.data.tChX.arr[y] = false;
      }
      Predpl.SetTo = pt;
      Predpl.SetDot = bDot;
      SetPredplDot(bDot);
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public boolean GetFin(PAllData p)
  {
      boolean c, c2;
      // заполнение поля
      c2 = true;
      for (int y = 1; y <= LenY; y++) {
          c = false; // флаг закончености строки
          for (int x = 1; x <= LenX; x++) {  // по строке
              c = c || (p.data.Data.arr[x][y] == 0); // если заполнено
          }
          c2 = c2 && (!c);
          p.data.FinX.arr[y] = !c;
  //        if (Predpl.B)  // массив флагов заполнености
  //            pDT.data.FinX.arr[y] = !c
  //            else pDM.data.FinX.arr[y] = !c;
      }
      for (int x = 1; x <= LenX; x++) {
          c = false; // флаг закончености строки
          for (int y = 1; y <= LenY; y++) {  // по строке
              c = c || (p.data.Data.arr[x][y] == 0); // если заполнено
          }
          c2 = c2 && (!c);
          p.data.FinY.arr[x] = !c;
  //        if (Predpl.B) // массив флагов заполнености
  //            pDT.data.FinY.arr[x] = !c
  //            else pDM.data.FinY.arr[x] = !c;
      }
      boolean Result = c2;
      if (Result) {
          if (p == pDM) return Result;
          ChangeDataArr((p == pDT));
      }
      return Result;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void PrepRjadX(PAllData p, int Y, TData Data, TRjad Rjad, int CountRjad){
      // подготовка строки
      for (int x = 1; x <= LenX; x++) {
          Data.arr[x] = p.data.Data.arr[x][Y]; // данные
      }
      CountRjad = CountRjadX.arr[Y]; // длинна ряда
      for (int x = 1; x <= CountRjadX.arr[Y]; x++) {
          Rjad.arr[x] = RjadX.arr[x][Y];  // сам ряд
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void PrepRjadY(PAllData p, int X, TData Data, TRjad Rjad, int CountRjad){
      // подготовка столбца
      for (int y = 1; y <= LenY; y++) {
          Data.arr[y] = p.data.Data.arr[X][y];  // данные
      }
      CountRjad = CountRjadY.arr[X]; // длинна ряда
      for (int y = 1; y <= CountRjadY.arr[X]; y++) {
          Rjad.arr[y] = RjadY.arr[X][y]; // сам ряд
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void AssignFile(TextFile f, String fileName) {

  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public String ReadLn(TextFile f) {
      return "";
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void WriteLn(TextFile f, String text) {
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ReSet(TextFile f) {

  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void CloseFile(TextFile f) {

  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ReWrite(TextFile f) {

  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void LoadRjadFromFile(String FileName) {
      TextFile F = new TextFile();
      String tstr;
      LoadFileName = FileName;
      AssignFile(F, FileName);
      ReSet(F);
      tstr = ReadLn(F);
      udCountX.Position = Integer.valueOf(tstr); // ширина
      tstr = ReadLn(F);
      udCountY.Position = Integer.valueOf(tstr); // высота
      for (int y = 1; y <= LenY; y++) {
          tstr = ReadLn(F);
          CountRjadX.arr[y] = Integer.valueOf(tstr); // длинна y строки
          for (int x = 1; x <= CountRjadX.arr[y]; x++) {
              tstr = ReadLn(F);
              RjadX.arr[x][y] = Integer.valueOf(tstr); // числа y строки
          }
      }

      for (int x = 1; x <= LenX; x++) {
          tstr = ReadLn(F);
          CountRjadY.arr[x] = Integer.valueOf(tstr);       // длинна х столбца
          for (int y = 1; y <= CountRjadY.arr[x]; y++) {
              tstr = ReadLn(F);
              RjadY.arr[x][y] = Integer.valueOf(tstr);   // числа х столбца
          }
      }
      CloseFile(F);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SaveRjadToFile(String FileName){
      TextFile F = new TextFile();
      LoadFileName = FileName;
      AssignFile(F, FileName);
      ReWrite(F);
      WriteLn(F, Integer.toString(LenX));  // ширина
      WriteLn(F, Integer.toString(LenY));  // высота
      for (int y = 1; y <= LenY; y++) {
          WriteLn(F, Integer.toString(CountRjadX.arr[y])); // длинна y строки
          for (int x = 1; x <= CountRjadX.arr[y]; x++) {
              WriteLn(F, Integer.toString(RjadX.arr[x][y])); // числа y строки
          }
      }

      for (int x = 1; x <= LenX; x++) {
          WriteLn(F, Integer.toString(CountRjadY.arr[x]));  // длинна х столбца
          for (int y = 1; y <= CountRjadY.arr[x]; y++) {
              WriteLn(F, Integer.toString(RjadY.arr[x][y]));  // числа х столбца
          }
      }
      CloseFile(F);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String ChangeFileExt(String fileName, String ext) {
      return "";
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String ExtractFileExt(String fileName) {
      return "";
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String ExtractFileName(String fileName) {
      return "";
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private boolean FileExists(String fileName) {
      return true;
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btSaveClick() {
      String tstr;
      String ext = "";
      if (cbMode.Checked) { // расширение по умолчанию (2 - файло редактора)
          sd.FilterIndex = 2;
      } else {
          sd.FilterIndex = 1;
      }
      switch (sd.FilterIndex) {
          case 1 : ext = ".jap"; break;
          case 2 : ext = ".jdt"; break;
      }
      if (sd.FileName != "") sd.FileName = ChangeFileExt(sd.FileName, ext);
      if (!sd.Execute()) {
          edInput.SetFocus();
          return; // запуск диалога
      }
      tstr = ExtractFileExt(sd.FileName); // расширение
      switch (sd.FilterIndex) {
          case 1 : ext = ".jap"; break;
          case 2 : ext = ".jdt"; break;
      }

      if (tstr != ext) { // если не те расширения ...
          sd.FileName = ChangeFileExt(sd.FileName, ext); //... то по умолчанию
      }
      // грузим файл
      od.FileName = sd.FileName;
      switch (sd.FilterIndex) {
          case 1 : SaveRjadToFile(sd.FileName);
          case 2 : SaveDataToFile(sd.FileName);
      }
      Form1.Caption = "Японские головоломки - " + ExtractFileName(sd.FileName);
      edInput.SetFocus();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btLoadClick()
  {
      String tstr, tstr2;
      String ext = "";
      boolean b; // флаг накладывания
      if (cbMode.Checked) { // расширение по умолчанию (2 - файло редактора)
          od.FilterIndex = 2;
      } else {
          od.FilterIndex = 1;
      }
      switch (od.FilterIndex) {
          case 1 : ext = ".jap"; break;
          case 2 : ext = ".jdt"; break;
      }
      if (od.FileName != "") od.FileName = ChangeFileExt(od.FileName, ext);
      if (!od.Execute()) {
          edInput.SetFocus();
          return; // запуск диалога
      }
      tstr = ExtractFileExt(od.FileName); // имя файла
      switch (od.FilterIndex) {
          case 1 : ext = ".jap"; break;
          case 2 : ext = ".jdt"; break;
      }
      if (tstr != ext) { // если не те расширения ...
          od.FileName = ChangeFileExt(od.FileName, ext); //... то по умолчанию
      }
      if (!FileExists(od.FileName)) {
          edInput.SetFocus();
          return;
      }
      sd.FileName = od.FileName;
      cbRjad.Checked = true;
      if (cbLoadNaklad.Checked) {
          tstr = ChangeFileExt(od.FileName, ".jap");
          tstr2 = ChangeFileExt(od.FileName, ".jdt");
          if (FileExists(tstr) && FileExists(tstr2)) {
              cbMode.Checked = false;
              cbMode.Caption = "Расш.";
              LoadRjadFromFile(tstr); // грузим файл
              LoadDataFromFile(tstr2);  // грузим файл
              bChangeLen = true;
              bUpDown = true;
              Draw(new TPoint(0, 0));
              Form1.Caption = "Японские головоломки - " + ExtractFileName(tstr) + ", " + ExtractFileName(tstr2);
              SetInfo(0, true, false, true, 0);
              return;
          }
      }
      b = (cbLoadNaklad.Checked && (cbMode.Checked ^ (od.FilterIndex == 2)));
      switch (od.FilterIndex) {
          case 1 : { // файл расшифровщика
              if (!b) {
                  // перекл режим
                  cbMode.Checked = false;
                  if (cbMode.Checked) {
                      cbMode.Caption = "Редактор";
                  } else {
                      cbMode.Caption = "Расш.";
                  }
                  ClearData(true); // очищаем поле
              }
              LoadRjadFromFile(od.FileName); // грузим файл
              bChangeLen = true;
              bUpDown = true;
              Draw(new TPoint(0, 0));
              Form1.Caption = "Японские головоломки - " + ExtractFileName(od.FileName);
              SetInfo(0, true, false, false, 0);
              if (!b) btCalc.Click();
          } break;
          case 2: { // файл редактора
              if (!b) {
                  // перекл режим
                  cbMode.Checked = true;
                  if (cbMode.Checked) {
                      cbMode.Caption = "Редактор";
                  } else {
                      cbMode.Caption = "Расш.";
                  }
              }
              LoadDataFromFile(od.FileName);  // грузим файл
              if (!b) {
                  GetRjadX(); // получаем ряды
                  GetRjadY();
              }
              bChangeLen = true;
              bUpDown = true;
              Draw(new TPoint(0, 0));
              Form1.Caption = "Японские головоломки - " + ExtractFileName(od.FileName);
              SetInfo(0, true, false, true, 0);
          } break;
      }
      edInput.SetFocus();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void LoadDataFromFile(String FileName) {
      String tstr;
      TextFile F = null;
      LoadFileName = FileName;
      AssignFile(F, FileName);
      ReSet(F);
      tstr = ReadLn(F);
      udCountX.Position = Integer.valueOf(tstr); // ширина
      tstr = ReadLn(F);
      udCountY.Position = Integer.valueOf(tstr); // высота
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++) {
              tstr = ReadLn(F);
              pDM.data.Data.arr[x][y] = Integer.valueOf(tstr); // поле
          }
      }
      CloseFile(F);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SaveDataToFile(String FileName) {
      TextFile F = null;
      LoadFileName = FileName;
      AssignFile(F, FileName);
      ReWrite(F);
      WriteLn(F, Integer.toString(LenX)); // ширина
      WriteLn(F, Integer.toString(LenY)); // высора
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++){
              WriteLn(F,Integer.toString(pDM.data.Data.arr[x][y])); // поле
          }
      }
      CloseFile(F);
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btClearClick() {
      ClearData(cbMode.Checked); // очищаем поле
      ChangeActive(new TPoint(1, 1), true); // записываем номер ячейки
      Draw(new TPoint(0, 0)); // прорисовка
      SetInfo(0, true, false, true, 0);
      edInput.SetFocus();
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ActiveNext(int c){
      if (CurrPt.xy) {
          if (c < 0) {
              if ((CurrPt.pt.x + c) <= 0) {
                  ChangeActive(new TPoint(LenX, 1), false); // записываем номер ячейки
              } else {
                  ChangeActive(new TPoint(CurrPt.pt.x + c, 1), CurrPt.xy); //следующая ячейка
              }
          } else {
              if ((CurrPt.pt.x + c) > LenY) {
                  ChangeActive(new TPoint(1, 1), false); // записываем номер ячейки
              } else {
                  ChangeActive(new TPoint(CurrPt.pt.x + c, 1), CurrPt.xy); //следующая ячейка
              }
          }
      } else {
          if (c < 0) {
              if ((CurrPt.pt.x + c) <= 0){
                  ChangeActive(new TPoint(LenY, 1), true); // записываем номер ячейки
          } else {
                  ChangeActive(new TPoint(CurrPt.pt.x + c, 1), CurrPt.xy); //следующая ячейка
              }
          } else {
              if ((CurrPt.pt.x + c) > LenX) {
                  ChangeActive(new TPoint(1, 1), true); // записываем номер ячейки
              } else {
                  ChangeActive(new TPoint(CurrPt.pt.x + c, 1), CurrPt.xy); //следующая ячейка
              }
          }
      }
      // делаем текст в едите выделенным
      edInput.SelectAll();
  //    edInput.Text = ""; // очищаем едит
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void edInputKeyDown(int Key, TShiftState Shift) {
      switch (Key) {
          case 38: { // вверх
              ActiveNext(-1);
              Key = 0;
          }
          break;
          case 39:
              break;
          case 40: { // вниз
              ActiveNext(1);
              Key = 0;
          }
          break;
          case 41:
              break;
      }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public int DecodeInteger(String str, char ch, int i) {
    return 0; // TODO надо реализовать
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void edInputKeyPress(char Key) {
      int a;
      String tstr = "";
      if (!(Arrays.asList(',', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\u0013', '\u0008').contains(Key))) {
          Key = '\u0000';
          return;
      }
      if (Key == ',') Key = '.';
      if (Key == '\u0013') {
          Key = '\u0000';
          tstr = edInput.Text;
          if (tstr.equals("")) return;
          // убираем дубл. точки
          int i = 2;
          do {
              if ((tstr.charAt(i - 1) == '.') && (tstr.charAt(i) == '.')) {
                  tstr = tstr.substring(1, i - 1) + tstr.substring(i + 1, tstr.length() - i);
              } else {
                  i++;
              }
              a = tstr.length();
          } while (i > a);
          // убираем точку спереди, добавляем в зад
          if (tstr.charAt(tstr.length() - 1) != '.') tstr = tstr + '.';
          if (tstr.charAt(1) == '.') tstr = tstr.substring(2, tstr.length() - 1);
          if (tstr.equals("")) return; // выходим если пусто
          a = DecodeInteger(tstr, '.', 0); // количество цифер
          if (!CurrPt.xy) { // столбцы
              // заполнение
              CountRjadY.arr[CurrPt.pt.x] = a;
              for (int j = 1; j <= a; j++) {
                  RjadY.arr[CurrPt.pt.x][j] = DecodeInteger(tstr, '.', j);
              }
              // проверка на ввод нулей - они не нужны
              i = 1;
              while (i <= a) {
                  if (RjadY.arr[CurrPt.pt.x][i] == 0) {
                      if (i != a) {
                          for (int j = i + 1; j <= a; j++) {
                              RjadY.arr[CurrPt.pt.x][j - 1] = RjadY.arr[CurrPt.pt.x][j];
                          }
                      }
                      CountRjadY.arr[CurrPt.pt.x] = CountRjadY.arr[CurrPt.pt.x] - 1;
                      a--;

                  } else {
                      i++;
                  }
              }
              if (CountRjadY.arr[CurrPt.pt.x] == 0) return;
              // проверка на ввод чила большего чем ширина
              int j = 0;
              for (i = 1; i <= a; i++) {
                  j = j + RjadY.arr[CurrPt.pt.x][i];
              }
              if ((j + a - 1) > LenY) {
                  CountRjadY.arr[CurrPt.pt.x] = 0;
                  Beep();
                  edInput.SelectAll();
                  return;
              }
              // прорисовать на поле если надо
              if (cbMode.Checked) DataFromRjadY(CurrPt.pt.x);
          } else { // строки
                  // заполнение
                  CountRjadX.arr[CurrPt.pt.x] = a;
                  for (i = 1; i <= a; i++) {
                      RjadX.arr[i][CurrPt.pt.x] = DecodeInteger(tstr, '.', i);
                  }
                  // проверка на ввод нулей - они не нужны
                  i = 1;
                  while (i <= a) {
                      if (RjadX.arr[i][CurrPt.pt.x] == 0) {
                          if (i != a) {
                              for (int j = i + 1; j <= a; j++) {
                                  RjadX.arr[j - 1][CurrPt.pt.x] = RjadX.arr[j][CurrPt.pt.x];
                              }
                          }
                          CountRjadX.arr[CurrPt.pt.x] = CountRjadX.arr[CurrPt.pt.x] - 1;
                          a--;
                      } else {
                          i++;
                      }
                  }
                  if (CountRjadX.arr[CurrPt.pt.x] == 0) return;
                  // проверка на ввод чила большего чем ширина
                  int j = 0;
                  for (i = 1; i <= a; i++) {
                      j = j + RjadX.arr[i][CurrPt.pt.x];
                  }
                  if ((j + a - 1) > LenX) {
                      CountRjadY.arr[CurrPt.pt.x] = 0;
                      Beep();
                      return;
                  }
                  // прорисовать на поле если надо
                  if (cbMode.Checked) DataFromRjadX(CurrPt.pt.x);
              }

          if (CurrPt.xy) {
              if (cbMode.Checked) {
                  Draw(new TPoint(0, CurrPt.pt.x));
              } else {
                  DrawLeft(CurrPt.pt.x);
                  Draw(new TPoint(-1, -1));
              }
          } else {
              if (cbMode.Checked) {
                  Draw(new TPoint(CurrPt.pt.x, 0));
              } else {
                  DrawTop(CurrPt.pt.x);
                  Draw(new TPoint(-1, -1));
              }
          }
          ActiveNext(1);
      }
  }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void Beep() {

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void ChangeActive(TPoint pt, boolean xy) {
      TCurrPt c = CurrPt;
      CurrPt.pt = pt;
      CurrPt.xy = xy;
      if (CurrPt.xy) {
          Draw(new TPoint(CurrPt.pt.x, 0));
      } else {
          Draw(new TPoint(0, CurrPt.pt.x));
      }
      if (c.xy) {
          Draw(new TPoint(c.pt.x, 0));
      } else {
          Draw(new TPoint(0, c.pt.x));
      }
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void cbRjadClick() {
      Draw(new TPoint(0, 0));
      edCountXChange();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btSaveBitmapClick() {
      String tstr;
      if (!spd.Execute()) {
          edInput.SetFocus();
          return;
      }
      cbRjad.Checked = true;
      tstr = ExtractFileExt(spd.FileName); // расширение
      if (tstr.equals("")) spd.FileName = spd.FileName + ".bmp"; // если нет разрешения то разрешение по умолчанию
      if (!tstr.equals(".bmp")) spd.FileName = ChangeFileExt(spd.FileName, ".bmp"); //если не те расширения то по умолчанию
      Buf.SaveToFile(spd.FileName);
      cbRjad.Checked = false;
      edInput.SetFocus();
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void btToWordClick() {
      WordXPRange RangeW;
      WordXPRange v1;
      WordXPRange v2;
      WordXPRange v3;
      TRows v4;
      OleVariant ov1, ov2;
      WordXPRow Row1;
      int i, k, ax, ay;
      String tstr;
      WordApplication1.Connect(); // запускаем ворд
      WordApplication1.Visible = true; // невидимый
      WordDocument1.Activate();  // новый документ

      ay = ((LenY + 1) / 2);
      ax = ((LenX + 1) / 2);
      for (int y = 1; y <= (LenY + ay); y++) { // по всем строкам
          tstr = "";
          if (y <= ay) { // ряды bmpTop
              for (int x = 1; x <= ax; x++) {
                  tstr = tstr + " " + '\u0009';
              }
              for (int x = 1; x <= LenX; x++) {
                  k = y - ay + CountRjadY.arr[x];
                  if (k > 0) {
                      tstr = tstr + Integer.toString(RjadY.arr[x][k]) + '\u0009';
                  } else {
                      tstr = tstr + " " + '\u0009';
                  }
              }
          } else { // ряды bmpLeft и само поле
              for (int x = 1; x <= (LenX + ax); x++) { // по всей строке
                  if (x <= ax) { // ряды bmpLeft
                      if (x <= (ax - CountRjadX.arr[y - ay])) {
                          tstr = tstr + " " + '\u0009';
                      } else {
                          tstr = tstr + Integer.toString(RjadX.arr[x - (ax - CountRjadX.arr[y - ay])][y - ay]) + '\u0009';
                      }
                  } else { // поле
                      tstr = tstr + " " + '\u0009';
                  }
              }
          }
          tstr = tstr.substring(1, tstr.length() - 1);
          WordDocument1.Range.InsertParagraphAfter();
          WordDocument1.ParagraphsLastRangeFontSize = 6;
          WordDocument1.ParagraphsLastRangeText = tstr;
      }

      RangeW = WordDocument1.Content();
      v1 = RangeW;
      v1.ConvertToTable('\u0009', 19, LenX + (LenX + 1) / 2);
      Row1 = WordDocument1.Tables_Item(1).Rows_Get_First();
      Row1.RangeFontSize = 1;
      Row1.RangeInsertParagraphAfter();
      ov1 = new OleVariant(" ");
      Row1.ConvertToText(ov1, ov1);

      v2 = WordDocument1.Tables_Item(1).Columns;
      for (int x = 1; x <= (ax + LenX); x++) {
          v2.Item(x).Width = 11;
      }

      v3 = WordDocument1.Tables_Item(1).Rows;
      for (int y = 1; y <= (ay + LenY); y++) {
          v3.Item(y).Height = 11;
      }

      v4 = WordDocument1.Tables_Item(1);
      for (int x = (ax + 1); x <= (ax + LenX); x++) {
          for (int y = (ay + 1); y <= (ay + LenY); y++) {
              if (pDM.data.Data.arr[x - ax][y - ay] == 1){
                  v1.Cell(y, x).ShadingBackgroundPatternColor = clDkGray;
              }
          }
      }

      WordDocument1.Tables_Item(1).RangeParagraphsFormatSpaceBefore = 0;
      WordDocument1.Tables_Item(1).RangeParagraphsFormatSpaceAfter = 0;
      WordDocument1.Tables_Item(1).RangeParagraphsFormatFirstLineIndent = 0;

      v1 = WordDocument1.Tables_Item(1).Columns;
      for (int x = (ax + 1); x <= (ax + LenX); x++) {
          v1.Item(x).BordersOutsideLineStyle = 1;

          v1.Item(x).CellsVerticalAlignment = wdCellAlignVerticalCenter;
      }

      v1 = WordDocument1.Tables_Item(1).Rows;
      for (int y = (ay + 1); y <= (ay + LenY); y++) {
          v1.Item(y).BordersOutsideLineStyle = 1;
          v1.Item(y).CellsVerticalAlignment = wdCellAlignVerticalCenter;
      }

      v1 = WordDocument1.Tables_Item(1).Columns;
      for (int x = ax; x <= (ax + LenX); x++) {
          if (((x - ax) % 5) == 0) {
              v1.Item(x).BordersItem(wdBorderRight).LineStyle = 7;
          }
      }

      v1 = WordDocument1.Tables_Item(1).Rows;
      for (int y = ay; y <= (ay + LenY); y++) {
          if (((y - ay) % 5) == 0) {
              v1.Item(y).BordersItem(wdBorderBottom).LineStyle = 7;
          }
      }

      WordDocument1.Tables_Item(1).Columns.BordersOutsideLineStyle = 7;

      WordApplication1.Visible = true; // видимый
      WordApplication1.Disconnect(); // отсоеденячемся от ворда
  }
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void udCountXChangingEx(boolean AllowChange, int NewValue, TUpDownDirection Direction) {
      switch (Direction) {
          case updUp : bUpDown = true; break;
          case updDown : bUpDown = false; break;
      }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void FormMouseMove(TShiftState Shift, int X, int Y){
      Label1.Caption = "";
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void CheckBox1Click() {
      btClear.Click();
      btCalc.Click();
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SetInfo(int Rjad, boolean bRjadStolb, boolean bPredpl, boolean bTimeNow, int bWhoPredpl) {
      int a;
      if (bTimeNow) t = Calendar.getInstance().getTime();
      if (bPredpl) {
          if (bWhoPredpl == 1) {
              Label2.Caption = "Расчет: предп. т.";
          } else {
              Label2.Caption = "Расчет: предп. п.";
          }
      } else {
          Label2.Caption = "Расчет: точно";
      }
      if (bRjadStolb) {
          Label4.Caption = String.format("Ряд: %s", Rjad);
      } else {
          Label4.Caption = String.format("Столбец: %s", Rjad);
      }
      Label5.Caption = Calendar.getInstance().getTime().toString();
      if (bPredpl) return;
      a = 0;
      for (int x = 1; x <= LenX; x++) {
          for (int y = 1; y <= LenY; y++) {
              if (pDM.data.Data.arr[x][y] > 0) {
                  a = a + 1;
              }
          }
      }
      Label3.Caption = "Открыто: " + Double.toString(Math.round(1000*a/(LenX*LenY))/10) + "%(" + Integer.toString(a) + ")";
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public void SaveTime() {
      TIniFile ini;
      String tstr, tstr2;
      if (LoadFileName.equals("")) {
          ShowMessage("Сохраните файл!");
          return;
      }
      ini = new TIniFile(ExtractFileDir(LoadFileName) + "Time.txt");
      try {
          tstr = ExtractFileName(LoadFileName);
          tstr2 = ini.ReadString(tstr, "_", "");
          ini.WriteString(tstr, "_", tstr2 + Label5.Caption.substring(7) + ", ");
      } finally {
          ini.Close();
      }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}
