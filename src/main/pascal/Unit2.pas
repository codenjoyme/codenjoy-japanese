unit Unit2;

interface

uses
  Windows, Classes;

Const MaxLen = 150;

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
  TVerArray = array [1..MaxLen] of Real;

var
    glData:TData;

    glVer:TVerArray;
    glCurrComb:TBitArray;
    glCombNum:integer;
    glLen:integer;

    glCR:integer;
    glRjad10:TRjad10;

    glRjad:TRjad;
    glCountRjad:byte;
    // обрез
    glCutFrom, glCutTo:Integer;
    glCutLen:integer;
    //------
    function  Cut:boolean;
    procedure GetRjadFromComb;
    procedure GetCombFromRjad;
    function  ManipuleRjad:boolean;
    function  TestComb:boolean;
    function  Calculate:boolean;

implementation
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function Calculate:boolean;
var i, j, i0, leni:integer;
    b1:boolean;
begin
    if (glCountRjad = 0) then begin
        Result:=true;
        for i:=1 to glLen do
            if (glData[i] = 1)
                then Result:=false
                else glData[i]:=2;
        Exit;
    end;
    //-----------
{    b1:=false;
    for i:=1 to glLen do begin
        b1:=b1 or (glData[i] <> 0);
        if (b1) then break;
    end;
    if (not b1) then begin
        j:=0;
        for i:=1 to glCountRjad do j:=j + glRjad[i];
        if (j < (glLen div 2)) then begin
            Result:=true;
            Exit;
        end;
    end; }
    //-----------
    for i:=1 to glLen do
        glVer[i]:=0;
    //-----------
    Result:=true;
    if (not Cut) then begin
        Result:=false;
        glCR:=1; j:=0;
        for i:=1 to glCountRjad do begin
            glRjad10[glCR].b:=true;
            glRjad10[glCR].c:=glRjad[i];
            glRjad10[glCR + 1].b:=false;
            glRjad10[glCR + 1].c:=1;
            j:=j + glRjad[i] + 1;
            glCR:=glCR + 2;
        end;
        glCR:=glCR - 1;
        if (j > glCutLen) then glCR:=glCR - 1;
        if (j < glCutLen) then glRjad10[glCR].c:=glRjad10[glCR].c + glCutLen - j;
        //-------
        b1:=true;
        glCombNum:=0;
        while (b1) do begin
            GetCombFromRjad;
            if (TestComb)
                then begin
                    glCombNum:=glCombNum + 1;

                    i0:=glCutFrom;
                    leni:=glCutTo;
                    for i:=i0 to leni do
                        if (glCurrComb[i])
                            then glVer[i]:=glVer[i] + 1;
                end;
            GetRjadFromComb;
            b1:=ManipuleRjad;
        end;

        for i:=glCutFrom to glCutTo do begin
            if (glCombNum <> 0)
                then glVer[i]:=glVer[i]/glCombNum
                else glVer[i]:=-1;
        end;
        if (glCombNum = 0)
            then Exit
            else Result:=true;
    end;
    //-----------
    for i:=1 to glLen do begin
        if (glVer[i] = 1) then glData[i]:=1;
        if (glVer[i] = 0) then glData[i]:=2;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TestComb: boolean;
var i:integer;
begin
    Result:=true;
    for i:=glCutFrom to glCutTo do begin
        case glData[i] of
            0:;                                         // ничего нет
            1:if (glCurrComb[i] <> true) then Result:=false;  // точка
            2:if (glCurrComb[i] <> false) then Result:=false; // пустота
            3:;                                         // предполагаемая точка
        end;
        if (not Result) then Exit;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure GetCombFromRjad;
var  i, j, x:integer;
begin
    x:=glCutFrom - 1;
    for i:=1 to glCR do begin
        for j:=1 to glRjad10[i].c do
            glCurrComb[x + j]:=glRjad10[i].b;
        x:=x + glRjad10[i].c;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure GetRjadFromComb;
var j, cr:integer;
    i, leni, i0:integer; 
    b:boolean;
begin
    b:=glCurrComb[glCutFrom];
    j:=1; cr:=1;

    i0:=(glCutFrom + 1);
    leni:=glCutTo;
    for i:=i0 to leni do begin
        if (glCurrComb[i] xor b) then begin
            glRjad10[cr].c:=j;
            glRjad10[cr].b:=b;
            inc(cr);
            j:=0;
        end;
        inc(j);
        b:=glCurrComb[i];
    end;
    if (j <> 0) then begin
        glRjad10[cr].c:=j;
        glRjad10[cr].b:=b;
    end;
    glCR:=cr;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function ManipuleRjad:boolean;
var i, a, a2:integer;
    b, b2:boolean;
begin
    a:=glCR;
    Result:=true;
    while (true) do begin
   {|}  if (glRjad10[a].b)
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
   {|}         {|}  if ((a + 2) > glCR) then begin
   {|}         {X------}if ((a + 2) <> (glCR + 1)) then break;
   {|}         {X------}if (not glRjad10[glCR].b) then break;
   {|}         {|} {|}
   {|}         {|} {|}  b2:=false;
   {|}         {|} {|}  if ((glRjad10[a].c <> 1) and (glCR = 2)) then b2:=true;
   {|}         {|} {|}  while (glRjad10[a].c = 1) do begin
   {|}         {|} {|} {|}  a:=a - 2;
   {|}         {|} {|} {|}  a2:=a2 + 2;
   {|}         {|} {|} {|}  if ((a <= 0) or (a2 = glCR)) then begin
   {|}         {|} {|} {|}      b2:=true;
   {|}         {|} {|} {X-------}break;
   {|}         {|} {|} {|}  end;
   {|}         {|} {|}  end;
   {|}         {X------}if (b2) then Break;
   {|}         {|} {|}
   {|}         {|} {|}  inc(glCR);
   {|}         {|} {|}  glRjad10[glCR].b:=false;
   {|}         {|} {|}  glRjad10[glCR].c:=0;
   {|}         {|}  end;
   {|}         {|}  glRjad10[a + a2].c:=glRjad10[a + a2].c + glRjad10[a].c - 2;
   {|}         {|}  glRjad10[a].c:=2;
   {|}         {X--}Break;
   {|}          end;
   {|}          if (b2) then begin
   {|}         {|}  Result:=false;
   {|--------------}Break;
   {|}          end;
   {|}      end
   {|}      else begin
   {|}     {|}  if (glCR = 1) then begin
   {|}     {|} {|}  Result:=false;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  if ((a - 2) <= 0) then begin
   {X--------------}if (a < 1) then Break;
   {|}     {|} {|}  inc(glCR);
   {|}     {|} {|}  for i:=(glCR - 1) downto 1 do begin
   {|}     {|} {|} {|}  glRjad10[i + 1].c:=glRjad10[i].c;
   {|}     {|} {|} {|}  glRjad10[i + 1].b:=glRjad10[i].b;
   {|}     {|} {|}  end;
   {|}     {|} {|}  glRjad10[1].c:=1;
   {|}     {|} {|}  glRjad10[1].b:=false;
   {|}     {|} {|}  if (glCR > 3) then glRjad10[3].c:=1;
   {|}     {|} {|}  if (glCR = 3) then glRjad10[3].c:=glRjad10[3].c - 1;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  glRjad10[a - 2].c:=glRjad10[a - 2].c + 1;
   {|}     {|}  glRjad10[a].c:=glRjad10[a].c - 1;
   {|}     {|}  if (glRjad10[a].c = 0)
   {|}     {|}      then begin
   {|}     {|}     {|}  if (a = glCR)
   {|}     {|}     {|}      then begin
   {|}     {|}     {|}     {|}  dec(glCR);
   {X---------------------------}Break;
   {|}     {|}     {|}      end
   {|}     {|}     {|}      else begin
   {|}     {|}     {|}     {|}  glRjad10[a - 2].c:=glRjad10[a - 2].c - 1;
   {|}     {|}     {|}     {|}  glRjad10[a].c:=glRjad10[a].c + 1;
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
function Cut:boolean;
var i, dr, cd:byte;
    b, bDot:boolean;
    //---------
    procedure SHLRjad;
    var j:byte;
    begin
        for j:=2 to glCountRjad do
            glRjad[j - 1]:=glRjad[j];
        glCountRjad:=glCountRjad - 1;
    end;
    //---------
begin
    b:=false; // выход из цикла
    i:=1; // по ряду
    bDot:=false; // предидущая - точка, нет
    dr:=0; // первый ряд точек
    cd:=0; // количество точек
    glCutFrom:=i;
    repeat
        case (glData[i]) of //
            0:  begin // ничего
                    if (bDot) // предидущая точка?
                        then begin // ничего после точки - надо заканчивать ряд
                            if (cd < glRjad[dr]) // dr - ый ряд закончили?
                                then begin // незакончили
                                    cd:=cd + 1; // количество точек
                                    glVer[i]:=1; // потом тут будет точка
                                    bDot:=true;  // поставили точку
                                end
                                else begin // закончили ряд
                                    cd:=0; // новы ряд еще не начали
                                    glVer[i]:=0; // потом тут будет пустота
                                    SHLRjad; // сдвигаем ряд (удаляем первый элемент)
                                    bDot:=false;  // поставили пустоту
                                    dr:=dr - 1; // из за смещения
                                end;
                        end
                        else begin // ничего после пустоты - выходим вообшето
                            if (glCountRjad = 0)
                                then begin // в этом ряде ничего больше делать нечего
                                    glVer[i]:=0; // канчаем его:)
                                end
                                else begin
                                    glCutFrom:=i;
                                    b:=true; // иначе выходим
                                end;
                        end;
                end;
            1:  begin // точка
                    if (not bDot) then begin
                        dr:=dr + 1;
                        cd:=0;
                        bDot:=true; // точка у нас
                    end;
                    glVer[i]:=1; // потом тут будет точка
                    cd:=cd + 1; // точек стало больше
                end;
            2:  begin // пустота
                    if (bDot) // предидущая - точка ?
                        then begin // да
                            if (cd <> glRjad[dr]) then begin
                                Result:=false;
                                Exit;
                            end;
                            bDot:=false; // теперь точки нет
                            SHLRjad; // сдвигаем ряд (удаляем первый элемент)
                            dr:=dr - 1; // из за смещения
                        end;
                    glVer[i]:=0; // пустота
                end;
        end;
        inc(i);
        if ((not b) and (i > glLen) or (glCountRjad = 0)) then begin // достигли конца
            Result:=true;
            Exit;
        end;
    until (b);

    b:=false; // выход из цикла
    i:=glLen; // по ряду
    bDot:=false; // предидущая - точка, нет
    dr:=glCountRjad + 1; // первый ряд точек
    cd:=0; // количество точек
    glCutTo:=i;
    repeat
        case (glData[i]) of //
            0:  begin // ничего
                    if (bDot) // предидущая точка?
                        then begin // ничего после точки - надо заканчивать ряд
                            if (cd < glRjad[dr]) // dr - ый ряд закончили?
                                then begin // незакончили
                                    cd:=cd + 1; // количество точек
                                    glVer[i]:=1; // потом тут будет точка
                                    bDot:=true;  // поставили точку
                                end
                                else begin // закончили ряд
                                    cd:=0; // новы ряд еще не начали
                                    glVer[i]:=0; // потом тут будет пустота
                                    glCountRjad:=glCountRjad - 1;
                                    bDot:=false; // поставили пустоту
                                end;
                        end
                        else begin // ничего после пустоты - выходим вообшето
                            if (glCountRjad = 0)
                                then begin // в этом ряде ничего больше делать нечего
                                    glVer[i]:=0; // канчаем его:)
                                end
                                else begin
                                    glCutTo:=i;
                                    b:=true; // иначе выходим
                                end;
                        end;
                end;
            1:  begin // точка
                    if (not bDot) then begin
                        dr:=dr - 1;
                        cd:=0;
                        bDot:=true; // точка у нас
                    end;
                    glVer[i]:=1; // потом тут будет точка
                    cd:=cd + 1; // точек стало больше
                end;
            2:  begin // пустота
                    if (bDot) // предидущая - точка ?
                        then begin // да
                            if (cd <> glRjad[dr]) then begin
                                Result:=false;
                                Exit;
                            end;
                            bDot:=false; // теперь точки нет
                            glCountRjad:=glCountRjad - 1;
                        end;
                    glVer[i]:=0; // пустота
                end;
        end;
        dec(i);
        if ((not b) and (i < 1) or (glCountRjad = 0)) then begin // достигли конца
            Result:=true;
            Exit;
        end;
    until (b);

    glCutLen:=glCutTo - glCutFrom + 1;
    Result:=((glCutFrom > glCutTo) or (glCountRjad = 0));
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
