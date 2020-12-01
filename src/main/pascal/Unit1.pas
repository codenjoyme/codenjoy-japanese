unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls, Unit2;

type
  TXYData = array [1..40, 1..40] of TDataPt;
  TXYRjad = array [1..40, 1..40] of byte;
  TForm1 = class(TForm)
    edCountX: TEdit;
    udCountX: TUpDown;
    pb: TPaintBox;
    btCalc: TButton;
    cbMode: TCheckBox;
    edCountY: TEdit;
    udCountY: TUpDown;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountXChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
    procedure cbModeClick(Sender: TObject);
  private
    PredCoord:TPoint; PredButt:TShiftState;
    bDown:boolean;
    Buf, bmpRight, bmpLeft, bmpPole:TBitMap;

    Data:TXYData;

    Comb:TComb;
    CountComb:integer;
    LenX, LenY:integer;

    RjadX, RjadY:TXYRjad;
    CountRjadX, CountRjadY:array [1..40] of integer;
    procedure Draw;
    procedure DrawPole;
    procedure DrawLeft;
    procedure DrawRight;
    procedure GetRjadX;
    procedure GetRjadY;
    procedure ClearData;
  public
    { Public declarations }
  end;

var Form1: TForm1;
const wid = 15;
implementation

{$R *.DFM}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearData;
var x, y:integer;
begin
    for x:=1 to LenX do
        for y:=1 to LenX do begin
            Data[x, y].Pos:=Point(x, y);
            Data[x, y].Ass:=Point(0, 0);
            Data[x, y].Check:=0;
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadX;
var x, y, a:integer;
begin
    for y:=1 to LenY do begin
        a:=0; CountRjadX[y]:=1;
        for x:=1 to LenX do begin
            if (Data[x, y].Check = 1)
                then inc(a)
                else
                    if (a <> 0) then begin
                        RjadX[CountRjadX[y], y]:=a;
                        a:=0;
                        inc(CountRjadX[y]);
                    end;
        end;
        if (a <> 0) then begin
            RjadX[CountRjadX[y], y]:=a;
            inc(CountRjadX[y]);
        end;
        dec(CountRjadX[y]);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadY;
var x, y, a:integer;
begin

end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.Draw;
begin
    DrawPole;
    DrawLeft;
    DrawRight;
    Buf.Width:=bmpLeft.Width + bmpPole.Width;
    Buf.Height:=bmpRight.Height + bmpPole.Height;
    Buf.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpRight.Height);
    Buf.Canvas.Draw(0,             bmpRight.Height, bmpLeft);
    Buf.Canvas.Draw(bmpLeft.Width, 0,               bmpRight);
    Buf.Canvas.Draw(bmpLeft.Width, bmpRight.Height, bmpPole);
    pb.Repaint;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft;
var x, y, tx, ty:integer;
    tstr:string;
begin
    bmpLeft.Width:={CountRjad}((LenX + 1) div 2)*wid;
    bmpLeft.Height:=LenY*wid;
    bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
    for y:=1 to LenY do
        for x:=1 to CountRjadX do begin
            tx:=(x - 1)*wid; ty:=(y - 1)*wid;
            bmpLeft.Canvas.Rectangle(tx, ty, x*wid, y*wid);
            tstr:=IntToStr(RjadX[x, y]);
            bmpLeft.Canvas.TextOut(tx + (wid - bmpLeft.Canvas.TextWidth(tstr) + 1) div 2,
                                   ty + (wid - bmpLeft.Canvas.TextHeight(tstr)) div 2, tstr);
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawRight;
var x, y, tx, ty:integer;
    tstr:string;
begin
    bmpRight.Width:=LenX*wid;
    bmpRight.Height:=((LenY + 1) div 2)*wid;
    bmpRight.Canvas.Rectangle(0, 0, bmpRight.Width, bmpRight.Height);
    for x:=1 to LenX do
        for y:=1 to CountRjadY do begin
            tx:=(x - 1)*wid; ty:=(y - 1)*wid;
            bmpRight.Canvas.Rectangle(tx, ty, x*wid, y*wid);
            tstr:=IntToStr(RjadY[x, y]);
            bmpRight.Canvas.TextOut(tx + (wid - bmpRight.Canvas.TextWidth(tstr) + 1) div 2,
                                    ty + (wid - bmpRight.Canvas.TextHeight(tstr)) div 2, tstr);
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawPole;
var x, y:integer;
begin
    bmpPole.Width:=LenX*wid;
    bmpPole.Height:=LenY*wid;
    bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
    for x:=1 to LenX do
        for y:=1 to LenX do begin
            case (Data[x, y].Check) of
                0: bmpPole.Canvas.Brush.Color:=clWhite;
                1: bmpPole.Canvas.Brush.Color:=clBlack;
                2: bmpPole.Canvas.Brush.Color:=clLtGray;
            end;
            bmpPole.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid, y*wid);
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormCreate(Sender: TObject);
begin
    PredCoord:=Point(-1, -1);
    PredButt:=[ssLeft];
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    bDown:=false;

    Buf:=TBitMap.Create;
    Buf.PixelFormat:=pf24bit;
    Buf.Width:=10;
    Buf.Height:=10;

    bmpLeft:=TBitMap.Create;
    bmpLeft.PixelFormat:=pf24bit;
    bmpLeft.Width:=10;
    bmpLeft.Height:=10;
    bmpLeft.Canvas.Pen.Color:=clBlack;
    bmpLeft.Canvas.Brush.Color:=clWhite;
    bmpLeft.Canvas.Font.Name:='Times New Roman';
    bmpLeft.Canvas.Font.Size:=7;

    bmpRight:=TBitMap.Create;
    bmpRight.PixelFormat:=pf24bit;
    bmpRight.Width:=10;
    bmpRight.Height:=10;
    bmpRight.Canvas.Pen.Color:=clBlack;
    bmpRight.Canvas.Brush.Color:=clWhite;
    bmpRight.Canvas.Font.Name:='Times New Roman';
    bmpRight.Canvas.Font.Size:=7;

    bmpPole:=TBitMap.Create;
    bmpPole.PixelFormat:=pf24bit;
    bmpPole.Width:=10;
    bmpPole.Height:=10;
    bmpPole.Canvas.Pen.Color:=clBlack;
    bmpPole.Canvas.Brush.Color:=clWhite;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormDestroy(Sender: TObject);
begin
    Buf.Free;
    bmpLeft.Free;
    bmpRight.Free;
    bmpPole.Free;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
var i, j, k, tx, ty:integer;
begin
    ty:=1;
    if (X < bmpLeft.Width) then begin
        X:=(X div wid) + 1;
        if (ssLeft in Shift) then begin
            j:=0;
            for tx:=1 to CountRjadX do
                j:=j + RjadX[tx, ty];
            if (X > CountRjadX) then begin
                X:=CountRjadX + 1;
                if ((j + CountRjadX) > LenX - 1) then Exit;
                inc(CountRjadX);
                RjadX[X, ty]:=0;
            end;
            if ((j + CountRjadX) > LenX) then Exit;
            RjadX[X, ty]:=RjadX[X, ty] + 1;
        end;
        if (ssRight in Shift) then begin
            if (CountRjadX = 0) then Exit;
            if (X > CountRjadX) then X:=CountRjadX;
            RjadX[X, ty]:=RjadX[X, ty] - 1;
            if (RjadX[X, ty] = 0) then begin
                if (X < CountRjadX) then
                    for i:=X to CountRjadX do
                        RjadX[i]:=RjadX[i + 1];
                dec(CountRjadX);
            end;
        end;

        if (cbMode.Checked) then begin
            ty:=1; ///!!!!
            for tx:=1 to LenX do Data[tx, ty].Check:=0;
            k:=1; tx:=1;
            while (tx <= CountRjadX) do begin
                for j:=1 to RjadX[tx, ty] do
                    Data[k + j - 1, ty].Check:=1;
                Data[k + j, ty].Check:=0;
                k:=k + j;
                inc(tx);
            end;
        end;
        
        Draw;
        Exit;
    end;
    bDown:=true;
    pbMouseMove(Sender, Shift, X, Y);    
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbPaint(Sender: TObject);
begin
    pb.Height:=Buf.Height;
    pb.Width:=Buf.Width;
    pb.Height:=Buf.Height;
    pb.Canvas.Draw(0, 0, Buf);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edCountXChange(Sender: TObject);
begin
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    ClearData;
    GetRjadX;
    GetRjadY;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var bDraw:boolean;
    tx, ty:integer;
begin
    ty:=1;
    if (not bDown) then Exit;
    X:=X - bmpLeft.Width;
    X:=(X div wid) + 1;
    if ((X <= 0) or (X > LenX)) then Exit;
    if ((PredButt = Shift) and (PredCoord.x = X)) then Exit;
    bDraw:=false;
    if (ssLeft in Shift) then begin
        bDraw:=(Data[X, ty].Check <> 1);
        Data[X, ty].Check:=1
    end;
    if (ssRight in Shift) then begin
        bDraw:=(Data[X, ty].Check <> 0);
        Data[X, ty].Check:=0;
    end;
    if (ssMiddle in Shift) then begin
        bDraw:=(Data[X, ty].Check <> 2);
        Data[X, ty].Check:=2;
    end;
    PredCoord:=Point(x, 1);
    PredButt:=Shift;
    if (bDraw) then begin
        if (cbMode.Checked) then begin
            GetRjadX;
            GetRjadY;            
        end;
        Draw;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    bDown:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.cbModeClick(Sender: TObject);
begin
    btCalc.Enabled:=not cbMode.Checked;
    GetRjadX;
    GetRjadY;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
begin
//    Init(Data, LenX, RjadX, CountRjadX);
    Calculate;
//    Finish(Data, LenX, RjadX, CountRjadX);
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
