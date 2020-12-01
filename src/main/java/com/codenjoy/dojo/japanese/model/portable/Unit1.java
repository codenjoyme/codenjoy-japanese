package com.codenjoy.dojo.japanese.model.portable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static com.codenjoy.dojo.japanese.model.portable.Unit2.*;

class Unit1 {

    public Unit2 Unit2 = new Unit2();
    public Date t; // TODO TdateTime // тут хранится время начала разгадывания кроссворда, с помощью нее вычисляется время расчета
    public TPoint PredCoord;
    TCheckBox cbMode = new TCheckBox();
    TOpenDialog od = new TOpenDialog();
    TSaveDialog sd = new TSaveDialog();
    TEdit edInput = new TEdit();
    TCheckBox cbRjad = new TCheckBox();
    TCheckBox cbVerEnable = new TCheckBox();
    boolean bDown; // непомню
    TCurrPt CurrPt = new TCurrPt();
    boolean bChangeLen, bUpDown; // флаг изменения размера кроссворда, флаг показывающий увеличился или уменшился кроссворд
    TAllData AllData1 = new TAllData(); // масивы данных
    TAllData AllData2 = new TAllData();
    TAllData AllData3 = new TAllData();
    PAllData pDM, pDT, pDP; // это указаьели на массивы данных
    TPredpl Predpl = new TPredpl(); //данные предположения
    int LenX = 15, LenY = 15; // длинна и высота кроссворда
    TXYRjad RjadX = new TXYRjad(); // тут хранятся цифры рядов
    TXYRjad RjadY = new TXYRjad();
    TXYCountRjad CountRjadX = new TXYCountRjad(); // тут хранятся количества цифер рядов
    TXYCountRjad CountRjadY = new TXYCountRjad();

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
    public void GetRjadX() {
        int a;
        for (int y = 1; y <= LenY; y++) {
            a = 0;
            CountRjadX.arr[y] = 1;
            for (int x = 1; x <= LenX; x++) {
                if (pDM.data.Data.arr[x][y] == 1) {
                    a++;
                } else {
                    if (a != 0) {
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
    public void GetRjadY() {
        int a;
        for (int x = 1; x <= LenX; x++) {
            a = 0;
            CountRjadY.arr[x] = 1;
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
    public void FormCreate() {
        pDM = new PAllData();
        pDT = new PAllData();
        pDP = new PAllData();
        pDM.data = AllData1;
        pDT.data = AllData2;
        pDP.data = AllData3;
        PredCoord = new TPoint(-1, -1);
        CurrPt.xy = true;
        CurrPt.pt = new TPoint(1, 1);
        //    edInput.SetFocus;
        bChangeLen = true;
        bUpDown = false;
        LenX = 15;
        LenY = 15;
        bDown = false;
        Predpl.SetDot = false;
        Predpl.B = false;

        ClearData(true);

        SetInfo(0, true, false, true, 0);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public void pbMouseDown(TMouseButton Button, TShiftState Shift, int X, int Y) {
//        int j, k;
//        Shift.remove(new String[]{ssDouble}); // когда 2 раза нажимаеш возникает и это сообщение, а оно портачит
//        if (edInput.Enabled) edInput.SetFocus(); // фокус едиту! :)
//        if ((X < bmpLeft.Width) && (Y < bmpTop.Height)) return; // если кликаем в области SmallBmp то выходим
//        if (X < bmpLeft.Width) { // тут находится bmpLeft
//            //        if (cbRjad.Checked) return; // это пока не проработал...
//            Y = Y - bmpTop.Height; // получаем координаты начала bmpLeft
//            X = X;
//            Y = (Y / wid) + 1; // теперь номер ячейки
//            X = (X / wid);
//
//            ChangeActive(new TPoint(Y, 0), true); // записываем номер ячейки
//
//            if (Shift.equals(new String[]{ssAlt, ssLeft})) { // если нужно расчитать этот ряд
//                Unit2.glCountRjad = PrepRjadX(pDM, Y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка ряда
//                Unit2.glLen = LenX; // длинна
//                if (!Unit2.Calculate()) { // расчет - если не получился ...
//                    ShowMessage("Ошибка в кроссворде (строка " + Integer.toString(Y) + ").");
//                    RefreshPole(); // обновляем поле
//                    return; // выходим
//                }
//                for (int x = 1; x <= LenX; x++) {// тут обновляем ряд
//                    pDM.data.Data.arr[x][Y] = Unit2.glData.arr[x];
//                    pDM.data.Ver.arr[x][Y][1] = Unit2.glVer.arr[x];
//                }
//                //            GetFin;
//                RefreshPole(); // и выводим его на екран
//                return; // выходим
//            }
//            // пересчет для координат для рядов
//            if (cbRjad.Checked) {
//                X = X + 1 - (GetMaxCountRjadX() - CountRjadX.arr[Y]);
//            } else {
//                X = X - (((LenX + 1) / 2) - CountRjadX.arr[Y]) + 1;
//            }
//
//            if (Shift.equals(new String[]{ssCtrl, ssLeft})) { // сдвиг рядов чисел
//                for (int ty = (Y + 1); ty <= LenY; ty++) {
//                    CountRjadX.arr[ty - 1] = CountRjadX.arr[ty];
//                    for (int tx = 1; tx <= CountRjadX.arr[ty]; tx++) {
//                        RjadX.arr[tx][ty - 1] = RjadX.arr[tx][ty];
//                    }
//                }
//                CountRjadX.arr[LenY] = 0;
//            }
//            if (Shift.equals(new String[]{ssCtrl, ssRight})) { // то же но в другую сторону
//                for (int ty = LenY; ty >= Y; ty--) {    // а возможен глюк
//                    CountRjadX.arr[ty] = CountRjadX.arr[ty - 1];
//                    for (int tx = 1; tx <= CountRjadX.arr[ty]; tx++) {
//                        RjadX.arr[tx][ty] = RjadX.arr[tx][ty - 1];
//                    }
//                }
//                CountRjadX.arr[Y] = 0;
//            }
//            if (Shift.equals(new String[]{ssLeft})) { // добавляем еще одну точку
//                // получаем сумму всех точек, включая пустые промежутки из длинной в 1
//                j = 0;
//                for (int tx = 1; tx <= CountRjadX.arr[Y]; tx++) {
//                    j = j + RjadX.arr[tx][Y];
//                }
//                if (X <= 0) { // добавление новой
//                    if ((j + CountRjadX.arr[Y]) > (LenX - 1))
//                        return; // если выходим при добавлении за граници, то выходим
//                    for (int tx = CountRjadX.arr[Y]; tx >= 1; tx--) {
//                        // смещаем все цифры ряда для добавления 1
//                        RjadX.arr[tx + 1][Y] = RjadX.arr[tx][Y];
//                    }
//                    X = 1; // позиция добавления
//                    CountRjadX.arr[Y] = CountRjadX.arr[Y] + 1; // длинна ряда увеличилась
//                    RjadX.arr[X][Y] = 0; // пока ноль...
//                }
//                if ((j + CountRjadX.arr[Y]) > LenX) return; // если превышаем длинну - то выходим
//                RjadX.arr[X][Y] = RjadX.arr[X][Y] + 1; // увеличиваем на 1
//            }
//            // тут уменьшаем на 1
//            if (Shift.equals(new String[]{ssRight})) {
//                if (CountRjadX.arr[Y] == 0) return; // если ряд пуст то выходим
//                if (X <= 0) X = 1; // если нажали на пустую ячейку, то удалять будем первый
//                RjadX.arr[X][Y] = RjadX.arr[X][Y] - 1; // удаляем
//                if (RjadX.arr[X][Y] == 0) { // если там ноль получился
//                    if (X != CountRjadX.arr[Y]) { // и этот ноль не в конце
//                        for (int tx = X; tx <= CountRjadX.arr[Y]; tx++) { // то сдвигаем
//                            RjadX.arr[tx][Y] = RjadX.arr[tx + 1][Y];
//                        }
//                    }
//                    CountRjadX.arr[Y] = CountRjadX.arr[Y] - 1; // уменьшаем на 1 количество
//                }
//            }
//            // тут прорисовка
//            if (cbMode.Checked) {
//                DataFromRjadX(Y);
//                DrawPole(new TPoint(0, Y));
//                DrawTop(0);
//            }
//            if ((Shift.equals(new String[]{ssCtrl, ssLeft}))
//                    || (Shift.equals(new String[]{ssCtrl, ssRight}))) {
//                DrawLeft(0);
//            } else {
//                DrawLeft(Y);
//            }
//            Draw(new TPoint(-1, -1));
//            return;
//        }
//        // далее то же, но только с рядами в bmpTop
//        if (Y < bmpTop.Height) {
//            Y = Y;
//            X = X - bmpLeft.Width;
//            Y = (Y / wid);
//            X = (X / wid) + 1;
//
//            ChangeActive(new TPoint(X, 0), false); // записываем номер ячейки
//
//            if (Shift.equals(new String[]{ssAlt, ssLeft})) {
//                Unit2.glCountRjad = PrepRjadY(pDM, X, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
//                Unit2.glLen = LenY;
//                if (!Unit2.Calculate()) {
//                    ShowMessage("Ошибка в кроссворде (столбец " + Integer.toString(X) + ").");
//                    RefreshPole();
//                    return;
//                }
//                for (int y = 1; y <= LenY; y++) {
//                    pDM.data.Data.arr[X][y] = Unit2.glData.arr[y];
//                    pDM.data.Ver.arr[X][y][2] = Unit2.glVer.arr[y];
//                }
//                //            GetFin();
//                RefreshPole();
//                return;
//            }
//
//            if (cbRjad.Checked) {
//                Y = Y + 1 - (GetMaxCountRjadY() - CountRjadY.arr[X]);
//            } else {
//                Y = Y - (((LenY + 1) / 2) - CountRjadY.arr[X]) + 1;
//            }
//            if (Shift.equals(new String[]{ssCtrl, ssLeft})) {
//                for (int tx = (X + 1); tx <= LenX; tx++) {
//                    CountRjadY.arr[tx - 1] = CountRjadY.arr[tx];
//                    for (int ty = 1; ty <= CountRjadY.arr[tx]; ty++) {
//                        RjadY.arr[tx - 1][ty] = RjadY.arr[tx][ty];
//                    }
//                }
//                CountRjadY.arr[LenX] = 0;
//            }
//            if (Shift.equals(new String[]{ssCtrl, ssRight})) {
//                for (int tx = LenX; tx >= X; tx--) {    // аозможен глюк
//                    CountRjadY.arr[tx] = CountRjadY.arr[tx - 1];
//                    for (int ty = 1; ty <= CountRjadY.arr[tx]; ty++) {
//                        RjadY.arr[tx][ty] = RjadY.arr[tx - 1][ty];
//                    }
//                }
//                CountRjadY.arr[X] = 0;
//            }
//            if (Shift.equals(new String[]{ssLeft})) {
//                j = 0;
//                for (int ty = 1; ty <= CountRjadY.arr[X]; ty++) {
//                    j = j + RjadY.arr[X][ty];
//                }
//                if (Y <= 0) {
//                    if ((j + CountRjadY.arr[X]) > (LenY - 1)) return;
//                    for (int ty = CountRjadY.arr[X]; ty >= 1; ty--) {
//                        RjadY.arr[X][ty + 1] = RjadY.arr[X][ty];
//                    }
//                    Y = 1;
//                    CountRjadY.arr[X] = CountRjadY.arr[X] + 1;
//                    RjadY.arr[X][Y] = 0;
//                }
//                if ((j + CountRjadY.arr[X]) > LenY) return;
//                RjadY.arr[X][Y] = RjadY.arr[X][Y] + 1;
//            }
//            if (Shift.equals(new String[]{ssRight})) {
//                if (CountRjadY.arr[X] == 0) return;
//                if (Y <= 0) Y = 1;
//                RjadY.arr[X][Y] = RjadY.arr[X][Y] - 1;
//                if (RjadY.arr[X][Y] == 0) {
//                    if (Y != CountRjadY.arr[X]) {
//                        for (int ty = Y; ty <= CountRjadY.arr[X]; ty++) {
//                            RjadY.arr[X][ty] = RjadY.arr[X][ty + 1];
//                        }
//                    }
//                    CountRjadY.arr[X] = CountRjadY.arr[X] - 1;
//                }
//            }
//
//            if (cbMode.Checked) {
//                DataFromRjadY(X);
//                DrawPole(new TPoint(X, 0));
//                DrawLeft(0);
//            }
//            if ((Shift.equals(new String[]{ssCtrl, ssLeft}))
//                    || (Shift.equals(new String[]{ssCtrl, ssRight}))) {
//                DrawTop(0);
//            } else {
//                DrawTop(X);
//            }
//            Draw(new TPoint(-1, -1));
//
//            return;
//        }
//        bDown = true;
//        pbMouseMove(Shift, X, Y); // а тут если на поле попали
//    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void DataFromRjadX(int y) {
        int k, tx, j;
        for (tx = 1; tx <= LenX; tx++) {
            pDM.data.Data.arr[tx][y] = 0;
        }
        k = 1;
        tx = 1;
        while (tx <= CountRjadX.arr[y]) {
            for (j = 1; j <= RjadX.arr[tx][y]; j++) {
                pDM.data.Data.arr[k + j - 1][y] = 1;
            }
            pDM.data.Data.arr[k + j][y] = 0;
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
                pDM.data.Data.arr[x][k + j - 1] = 1;
            }
            pDM.data.Data.arr[x][k + j] = 0;
            k = k + j;
            ty++;
        }
        GetRjadX();
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public void pbMouseMove(TShiftState Shift, int X, int Y) {
//        boolean bDraw;
//        Y = Y - bmpTop.Height;
//        X = X - bmpLeft.Width;
//        if ((X < 0) || (Y < 0)) {
//            Label1.Caption = "";
//            return;
//        }
//        Y = (Y / wid) + 1;
//        X = (X / wid) + 1;
//        if ((Y <= 0) || (Y > LenY) || (X <= 0) || (X > LenX)) {
//            Label1.Caption = "";
//            return;
//        }
//        Label1.Caption = Double.toString(Math.round(pDM.data.Ver.arr[X][Y][1] * 100) / 100) + "     " + Double.toString(Math.round(pDM.data.Ver.arr[X][Y][2] * 100) / 100);
//        if (!bDown) return;
//        bDraw = false;
//        if (Shift.equals(new String[]{ssLeft})) {
//            bDraw = (pDM.data.Data.arr[X][Y] != 1);
//            pDM.data.Data.arr[X][Y] = 1;
//        }
//        if (Shift.equals(new String[]{ssRight})) {
//            bDraw = (pDM.data.Data.arr[X][Y] != 0);
//            pDM.data.Data.arr[X][Y] = 0;
//        }
//        if (Shift.equals(new String[]{ssMiddle})) {
//            bDraw = (pDM.data.Data.arr[X][Y] != 2);
//            pDM.data.Data.arr[X][Y] = 2;
//        }
//        PredCoord = new TPoint(X, Y);
//        PredButt = Shift;
//        if (bDraw) {
//            if (cbMode.Checked) {
//                GetRjadX();
//                GetRjadY();
//                DrawLeft(Y);
//                DrawTop(X);
//            }
//            DrawPole(new TPoint(X, Y));
//            Draw(new TPoint(-1, -1));
//        }
//    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public void cbModeClick() {
//        btCalc.Enabled = !cbMode.Checked;
//        cbVerEnable.Enabled = btCalc.Enabled;
//        if (cbMode.Checked) {
//            cbMode.Caption = "Редактор";
//        } else {
//            cbMode.Caption = "Расш.";
//        }
//        if (cbMode.Checked) {
//            GetRjadX();
//            GetRjadY();
//        }
//    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public TPoint Check() {
        int a1, a2;
        a1 = 0;
        for (int x = 1; x <= LenX; x++) {
            for (int y = 1; y <= CountRjadY.arr[x]; y++) {
                a1 = a1 + RjadY.arr[x][y];
            }
        }
        a2 = 0;
        for (int y = 1; y <= LenY; y++) {
            for (int x = 1; x <= CountRjadX.arr[y]; x++) {
                a2 = a2 + RjadX.arr[x][y];
            }
        }
        return new TPoint(a1, a2);  // разница рядов
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void RefreshPole() {

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void btCalcClick() {
        boolean b, b2, b5, b6, b7, b8, b9, b11; // b - произошли ли изменения, b2 - была ли ошибка, b3 - , b4 - , b5 - предполагать максимальной вероятности с учетом массива NoSet, b6 - если точка с максимальной вероятностью была найдена, b7 - последний прогон для нормального отображения вероятностей, b8 - если нажали остановить, b9 - если остановка по ошибке, b11 - нудно для пропуска прогона по у если LenX больше LenY
        int h; 
        double MaxVer1, MaxVer2; 
        double a1, a2; 
        TPoint pt;
        PAllData pWork;
        boolean bErrT, bErrP;

        t = Calendar.getInstance().getTime(); //

        // проверка на совпадение рядов
        pt = Check();
        int x0 = Math.abs(pt.x - pt.y);
        if (x0 > 0) {
            System.out.println("Ошибка! Несовпадение на " + x0);
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
        b11 = (a1 / LenY > a2 / LenX);
        //----------------------------------------------------------
        Predpl.B = false;
        b = false; // для b11
        bErrT = false;
        bErrP = false;
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
                    if (pWork.data.FinX.arr[y]) continue;
                    if (!pWork.data.ChX.arr[y]) continue;
                    Unit2.glCountRjad = PrepRjadX(pWork, y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка строки
                    Unit2.glLen = LenX; // длинна строки
//                    if (bPredpl) {
//                        if (Predpl.SetDot == 1) {
//                            System.out.println("Ряд: " + Integer.toString(y) + " предп. т");
//                        } else {
//                            System.out.println(("Ряд: " + Integer.toString(y) + " предп. п");
//                        }
//                    } else {
//                        System.out.println(("Ряд: " + Integer.toString(y) + " точно");
//                    }
                    if (!Unit2.Calculate()) { // расчет ... если нет ни одной комбины - ошибка
                        if (!cbVerEnable.Checked) {
                            System.out.println("Ошибка в кроссворде (строка " + Integer.toString(y) + ").");
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
                    if (pWork.data.FinY.arr[x]) continue;
                    if (!pWork.data.ChY.arr[x]) continue;
                    Unit2.glCountRjad = PrepRjadY(pWork, x, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
                    Unit2.glLen = LenY;
//                    if (bPredpl) {
//                        if (Predpl.SetDot == 1) {
//                            System.out.println(("Ст.: " + Integer.toString(x) + " предп. т")
//                        } else {
//                            System.out.println(("Ст.: " + Integer.toString(x) + " предп. п");
//                        }
//                    } else {
//                        System.out.println(("Ст.: " + Integer.toString(x) + " точно");
//                    }
                    if (!Unit2.Calculate()) {
                        if (!cbVerEnable.Checked) {
                            System.out.println("Ошибка в кроссворде (столбец " + Integer.toString(x) + ").");
                            b9 = true;
                            break;
                        }
                        b = false; // изменений нету
                        b2 = true; // ошибка была
                        break;
                    }

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
                                System.out.println("Ошибка в кроссворде.");
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
                        System.out.println("Ошибка в кроссворде.");
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

                        b6 = b6 && ((MaxVer1 > 0) || (MaxVer2 > 0)); // критерий отбора
                        if (b6) { // нашли точку?
                            // да
                            if (!b5) {
                                Predpl.B = true;
                                SavePustot(pt, true); // сохраняемся только если искали макс вероятность без учета массива NoSet
                            }
                            pDM.data.Data.arr[pt.x][pt.y] = 3;
                            Draw(pt);
//System.out.println(("Предп. в " + Integer.toString(pt.y) + ", " + Integer.toString(pt.x));
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
            // решили кроссворд!
        }

        RefreshPole(); // прорисовка поля
        SetInfo(0, true, false, false, 0);
    }

    private void Draw(TPoint pt) {

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void SetPredplDot(boolean bDot) {
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
    public void ChangeDataArr(boolean bDot) {
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
        boolean b;
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
    public boolean GetFin(PAllData p) {
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
    public int PrepRjadX(PAllData p, int Y, TData Data, TRjad Rjad, int CountRjad) {
        // подготовка строки
        for (int x = 1; x <= LenX; x++) {
            Data.arr[x] = p.data.Data.arr[x][Y]; // данные
        }
        int result = CountRjadX.arr[Y]; // длинна ряда
        for (int x = 1; x <= CountRjadX.arr[Y]; x++) {
            Rjad.arr[x] = RjadX.arr[x][Y];  // сам ряд
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public int PrepRjadY(PAllData p, int X, TData Data, TRjad Rjad, int CountRjad) {
        // подготовка столбца
        for (int y = 1; y <= LenY; y++) {
            Data.arr[y] = p.data.Data.arr[X][y];  // данные
        }
        int result = CountRjadY.arr[X]; // длинна ряда
        for (int y = 1; y <= CountRjadY.arr[X]; y++) {
            Rjad.arr[y] = RjadY.arr[X][y]; // сам ряд
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AssignFile(TextFile f, String fileName) {
        f.open(fileName);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public String ReadLn(TextFile f) {
        return f.readLine();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void WriteLn(TextFile f, String text) {
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void ReSet(TextFile f) {
        f.loadData();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void CloseFile(TextFile f) {
        f.close();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void ReWrite(TextFile f) {

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void LoadRjadFromFile(String FileName) {
        TextFile F = new TextFile();
        String tstr;
        AssignFile(F, FileName);
        ReSet(F);
        tstr = ReadLn(F);
        LenX = Integer.valueOf(tstr); // ширина
        tstr = ReadLn(F);
        LenY = Integer.valueOf(tstr); // высота
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
    public void SaveRjadToFile(String FileName) {
        TextFile F = new TextFile();
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
        return fileName.substring(0, fileName.lastIndexOf(".")) + ext;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String ExtractFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String ExtractFileName(String fileName) {
        return new File(fileName).getName();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private boolean FileExists(String fileName) {
        return new File(fileName).exists();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void btSaveClick(int FilterIndex, String FileName) {
        switch (FilterIndex) {
            case 1:
                SaveRjadToFile(FileName);
            case 2:
                SaveDataToFile(FileName);
        }
        System.out.println("Японские головоломки - " + ExtractFileName(FileName));
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void btLoadClick(boolean cbLoadNaklad, String FileName, int FilterIndex) {
        String tstr, tstr2;
        boolean b; // флаг накладывания

        cbRjad.Checked = true;
        if (cbLoadNaklad) {
            tstr = ChangeFileExt(FileName, ".jap");
            tstr2 = ChangeFileExt(FileName, ".jdt");
            if (FileExists(tstr) && FileExists(tstr2)) {
                cbMode.Checked = false;
                cbMode.Caption = "Расш.";
                LoadRjadFromFile(tstr); // грузим файл
                LoadDataFromFile(tstr2);  // грузим файл
                bChangeLen = true;
                bUpDown = true;
                Draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + ExtractFileName(tstr) + ", " + ExtractFileName(tstr2));
                SetInfo(0, true, false, true, 0);
                return;
            }
        }
        b = (cbLoadNaklad && (cbMode.Checked ^ (FilterIndex == 2)));
        switch (FilterIndex) {
            case 1: { // файл расшифровщика
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
                LoadRjadFromFile(FileName); // грузим файл
                bChangeLen = true;
                bUpDown = true;
                Draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + ExtractFileName(FileName));
                SetInfo(0, true, false, false, 0);
            }
            break;
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
                LoadDataFromFile(FileName);  // грузим файл
                if (!b) {
                    GetRjadX(); // получаем ряды
                    GetRjadY();
                }
                bChangeLen = true;
                bUpDown = true;
                Draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + ExtractFileName(FileName));
                SetInfo(0, true, false, true, 0);
            }
            break;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void LoadDataFromFile(String FileName) {
        String tstr;
        TextFile F = null;
        AssignFile(F, FileName);
        ReSet(F);
        tstr = ReadLn(F);
        LenX = Integer.valueOf(tstr); // ширина
        tstr = ReadLn(F);
        LenY = Integer.valueOf(tstr); // высота
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
        AssignFile(F, FileName);
        ReWrite(F);
        WriteLn(F, Integer.toString(LenX)); // ширина
        WriteLn(F, Integer.toString(LenY)); // высора
        for (int x = 1; x <= LenX; x++) {
            for (int y = 1; y <= LenY; y++) {
                WriteLn(F, Integer.toString(pDM.data.Data.arr[x][y])); // поле
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
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void ActiveNext(int c) {
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
                if ((CurrPt.pt.x + c) <= 0) {
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
//                    DrawLeft(CurrPt.pt.x);
                    Draw(new TPoint(-1, -1));
                }
            } else {
                if (cbMode.Checked) {
                    Draw(new TPoint(CurrPt.pt.x, 0));
                } else {
//                    DrawTop(CurrPt.pt.x);
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void SetInfo(int Rjad, boolean bRjadStolb, boolean bPredpl, boolean bTimeNow, int bWhoPredpl) {
        int a;
        if (bTimeNow) t = Calendar.getInstance().getTime();
        if (bPredpl) {
            if (bWhoPredpl == 1) {
                System.out.println("Расчет: предп. т.");
            } else {
                System.out.println("Расчет: предп. п.");
            }
        } else {
            System.out.println("Расчет: точно");
        }
        if (bRjadStolb) {
            System.out.println(String.format("Ряд: %s", Rjad));
        } else {
            System.out.println(String.format("Столбец: %s", Rjad));
        }
        System.out.println(Calendar.getInstance().getTime().toString());
        if (bPredpl) return;
        a = 0;
        for (int x = 1; x <= LenX; x++) {
            for (int y = 1; y <= LenY; y++) {
                if (pDM.data.Data.arr[x][y] > 0) {
                    a = a + 1;
                }
            }
        }
        System.out.println("Открыто: " + Double.toString(Math.round(1000 * a / (LenX * LenY)) / 10) + "%(" + a + ")");
    }

    static class TXYData {
        public int[][] arr = new int[MaxLen + 1][MaxLen + 1]; // TODO array 1..MaxLen, 1..MaxLen
    }

    static class TXYPustot {
        public boolean[][] arr = new boolean[MaxLen + 1][MaxLen + 1]; // TODO array 1..MaxLen, 1..MaxLen
    }

    static class TXYRjad {
        public int[][] arr = new int[MaxLen + 1][MaxLen + 1]; // TODO array 1..MaxLen, 1..MaxLen
    }

    static class TFinish {
        public boolean[] arr = new boolean[MaxLen + 1]; // TODO  array 1..MaxLen
    }

    static class TXYCountRjad {
        public int[] arr = new int[MaxLen + 1]; // TODO  array 1..MaxLen
    }

    static class TXYVer {
        public double[][][] arr = new double[MaxLen + 1][MaxLen + 1][3]; // TODO [1..MaxLen, 1..MaxLen, 1..2] of Real;
    }

    static class TAllData {
        public TXYVer Ver = new TXYVer();
        public TFinish FinX = new TFinish();
        public TFinish FinY = new TFinish();
        public TFinish ChX = new TFinish();
        public TFinish ChY = new TFinish();
        public TXYData Data = new TXYData();
        public TFinish tChX = new TFinish();
        public TFinish tChY = new TFinish();
        public TXYPustot NoSet = new TXYPustot();
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
        public String Text;

        public void SetFocus() {
        }

        public void SelectAll() {

        }
    }

    static class TUpDown {
        public int Max;
        public int Position;
    }

    static class TPaintBox {
    }

    static class TButton {
        public int Tag;
        public String Caption;

        public void Click() {

        }
    }

    static class TCheckBox {
        public boolean Checked;
        public String Caption;
    }

    static class TOpenDialog {
        public String FileName;
        public int FilterIndex;

        public boolean Execute() {
            return true;
        }
    }

    static class TSaveDialog {
        public int FilterIndex;
        public String FileName;

        public boolean Execute() {
            return false;
        }
    }

    static class TSavePictureDialog {
    }

    static class TWordApplication {

    }


    static class TWordDocument {

    }

    static class TPanel {

    }

    static class TLabel {
        public String Caption;
    }

    static class TGroupBox {

    }


    static class TextFile {
        File file;
        List<String> lines;
        int index;

        public void open(String fileName) {
            file = new File(fileName);
            lines = new LinkedList<>();
        }

        public void loadData() {
            try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
                stream.forEach(lines::add);
                index = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String readLine() {
            return lines.get(index++);
        }

        public void close() {
            lines.clear();
            index = 0;
        }
    }

    public static class TShiftState {
    }
}
