unit Unit2;

interface

uses
  Windows, Classes;

Const MaxLen = 50;

type
  TDataPt = record
     Pos, Ass:TPoint;
     Check:byte; // 0 - пусто 1 - чек 2 - нечек
  end;
  TData = array [1..MaxLen] of byte;
  PData = ^TData;
  TRjad10 = array [1..MaxLen] of record
    c:byte;
    b:boolean;
  end;
  TRjad = array [1..MaxLen] of byte;
  PRjad = ^TRjad;
  TBitArray = array [1..MaxLen] of boolean;
  TComb = array [1..10000000] of TBitArray;

var
    glData:TData;

    glComb1, glComb2:TBitArray;
    glCurrComb:TBitArray;
    glLen:integer;


    glRjad:TRjad;
    glCountRjad:integer;

    procedure GetRjadFromComb(var r: TRjad10; var cr:integer; var bits:TBitArray); // тут р€д включает количества чередующихс€ пустых и непустых! €чеек
    procedure GetCombFromRjad(var r: TRjad10; var cr:integer; var bits:TBitArray); // тут р€д включает количества чередующихс€ пустых и непустых! €чеек
    function  ManipuleRjad(var r: TRjad10; var cr:integer):boolean;
    function  TestComb(var dt:TData; l:integer; var bits:TBitArray):boolean;
    function  Calculate:boolean;

implementation
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function Calculate:boolean;
var i, j:integer;
    b1, b2:boolean;
    Rjad10:TRjad10;
    cr:integer;
begin
    Result:=false;
    if (glCountRjad = 0) then begin
        Result:=true;
        for i:=1 to glLen do
            if (glData[i] = 1)
                then Result:=false
                else glData[i]:=2;
        Exit;
    end;
    //-----------
    for i:=1 to glLen do begin
        glComb1[i]:=true;
        glComb2[i]:=false;
    end;
    //-----------
    cr:=1; j:=0;
    for i:=1 to glCountRjad do begin
        Rjad10[cr].b:=true;
        Rjad10[cr].c:=glRjad[i];
        Rjad10[cr + 1].b:=false;
        Rjad10[cr + 1].c:=1;
        j:=j + glRjad[i] + 1;
        cr:=cr + 2;
    end;
    cr:=cr - 1;
    if (j > glLen) then cr:=cr - 1;
    if (j < glLen) then Rjad10[cr].c:=Rjad10[cr].c + glLen - j;
    //-------
    b1:=true;
    b2:=false;
    while (b1) do begin
        GetCombFromRjad(Rjad10, cr, glCurrComb);
        if (TestComb(glData, glLen, glCurrComb))
            then begin
                b2:=true;
                for i:=1 to glLen do begin
                    glComb1[i]:=glComb1[i] and glCurrComb[i];
                    glComb2[i]:=glComb2[i] or  glCurrComb[i];
                end;
            end;
        GetRjadFromComb(Rjad10, cr, glCurrComb);
        b1:=ManipuleRjad(Rjad10, cr);
    end;
    if (not b2)
        then Exit
        else Result:=true;
    //-----------
    for i:=1 to glLen do begin
        if (glComb1[i]) then glData[i]:=1;
        if (not glComb2[i]) then glData[i]:=2;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TestComb(var dt: TData; l:integer; var bits: TBitArray): boolean;
var i:integer;
    a:integer;
begin
    Result:=true;
    for i:=1 to l do begin
        case dt[i] of
            0:;
            1:if (bits[i] <> true) then Result:=false;
            2:if (bits[i] <> false) then Result:=false;
        end;
        if (not Result) then Exit;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure GetCombFromRjad(var r: TRjad10; var cr:integer; var bits:TBitArray);
var  i, j, x:integer;
begin
    x:=0;
    for i:=1 to cr do begin
        for j:=1 to r[i].c do
            bits[x + j]:=r[i].b;
        x:=x + r[i].c;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure GetRjadFromComb(var r: TRjad10; var cr: integer; var bits: TBitArray);
var i, j:integer;
    b:boolean;
begin
    b:=bits[1];
    j:=1; cr:=1;
    for i:=2 to glLen do begin
        if (bits[i] xor b) then begin
            r[cr].c:=j;
            r[cr].b:=b;
            inc(cr);
            j:=0;
        end;
        inc(j);
        b:=bits[i];
    end;
    if (j <> 0) then begin
        r[cr].c:=j;
        r[cr].b:=b;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function ManipuleRjad(var r: TRjad10; var cr: integer):boolean;
var i, a, a2:integer;
    b, b2:boolean;
begin
    a:=cr;
    Result:=true;
    while (true) do begin
   {|}  if (r[a].b)
   {|}      then begin
   {|}          a:=a - 1;
   {|}          if (a <= 0) then begin
   {|}              Result:=false;
   {|--------------}Break;
   {|}          end;
   {|}          b:=true;
   {|}          while true do begin
   {|}         {|}  a2:=2;
   {|}         {|}  if (not b)
   {|}         {|------}then Continue;
   {|}         {|}  b:=not b;
   {|}         {|}
   {|}         {|}  if ((a + 2) > cr) then begin
   {|}         {X------}if ((a + 2) <> (cr + 1)) then break;
   {|}         {X------}if (not r[cr].b) then break;
   {|}         {|} {|}
   {|}         {|} {|}  b2:=false;
   {|}         {|} {|}  if ((r[a].c <> 1) and (cr = 2)) then b2:=true;
   {|}         {|} {|}  while (r[a].c = 1) do begin
   {|}         {|} {|} {|}  a:=a - 2;
   {|}         {|} {|} {|}  a2:=a2 + 2;
   {|}         {|} {|} {|}  if ((a <= 0) or (a2 = cr)) then begin
   {|}         {|} {|} {|}      b2:=true;
   {|}         {|} {|} {X-------}break;
   {|}         {|} {|} {|}  end;
   {|}         {|} {|}  end;
   {|}         {X------}if (b2) then Break;
   {|}         {|} {|}
   {|}         {|} {|}  inc(cr);
   {|}         {|} {|}  r[cr].b:=false;
   {|}         {|} {|}  r[cr].c:=0;
   {|}         {|}  end;
   {|}         {|}  r[a + a2].c:=r[a + a2].c + r[a].c - 2;
   {|}         {|}  r[a].c:=2;
   {|}         {X--}Break;
   {|}          end;
   {|}          if (b2) then begin
   {|}         {|}  Result:=false;
   {|--------------}Break;
   {|}          end;
   {|}      end
   {|}      else begin
   {|}     {|}  if (cr = 1) then begin
   {|}     {|} {|}  Result:=false;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  if ((a - 2) <= 0) then begin
   {X--------------}if (a < 1) then Break;
   {|}     {|} {|}  inc(cr);
   {|}     {|} {|}  for i:=(cr - 1) downto 1 do begin
   {|}     {|} {|} {|}  r[i + 1].c:=r[i].c;
   {|}     {|} {|} {|}  r[i + 1].b:=r[i].b;
   {|}     {|} {|}  end;
   {|}     {|} {|}  r[1].c:=1;
   {|}     {|} {|}  r[1].b:=false;
   {|}     {|} {|}  if (cr > 3) then r[3].c:=1;
   {|}     {|} {|}  if (cr = 3) then r[3].c:=r[3].c - 1;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  r[a - 2].c:=r[a - 2].c + 1;
   {|}     {|}  r[a].c:=r[a].c - 1;
   {|}     {|}  if (r[a].c = 0)
   {|}     {|}      then begin
   {|}     {|}     {|}  if (a = cr)
   {|}     {|}     {|}      then begin
   {|}     {|}     {|}     {|}  dec(cr);
   {X---------------------------}Break;
   {|}     {|}     {|}      end
   {|}     {|}     {|}      else begin
   {|}     {|}     {|}     {|}  r[a - 2].c:=r[a - 2].c - 1;
   {|}     {|}     {|}     {|}  r[a].c:=r[a].c + 1;
   {|}     {|}     {|}     {|}  a:=a - 2;
   {|}     {|}     {|}      end;
   {|---------------------}continue;
   {|}     {|}      end
   {|}     {|}      else begin
//   {|}     {|}     {|}   dec(cr);
   {X---------------------}Break;
   {|}     {|}      end;
   {|}      end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
