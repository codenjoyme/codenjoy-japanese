object Form1: TForm1
  Left = 75
  Top = 104
  Width = 585
  Height = 563
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object pb: TPaintBox
    Left = 80
    Top = 8
    Width = 489
    Height = 513
    OnMouseDown = pbMouseDown
    OnMouseMove = pbMouseMove
    OnMouseUp = pbMouseUp
    OnPaint = pbPaint
  end
  object edCountX: TEdit
    Left = 8
    Top = 8
    Width = 57
    Height = 21
    TabOrder = 0
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountX: TUpDown
    Left = 65
    Top = 8
    Width = 12
    Height = 21
    Associate = edCountX
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 1
    Wrap = False
  end
  object btCalc: TButton
    Left = 8
    Top = 104
    Width = 65
    Height = 25
    Caption = '&Расчет'
    TabOrder = 2
    OnClick = btCalcClick
  end
  object cbMode: TCheckBox
    Left = 8
    Top = 80
    Width = 65
    Height = 17
    Caption = 'Расш.'
    TabOrder = 3
    OnClick = cbModeClick
  end
  object edCountY: TEdit
    Left = 8
    Top = 32
    Width = 57
    Height = 21
    TabOrder = 4
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountY: TUpDown
    Left = 65
    Top = 32
    Width = 12
    Height = 21
    Associate = edCountY
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 5
    Wrap = False
  end
  object btSave: TButton
    Left = 8
    Top = 136
    Width = 65
    Height = 25
    Caption = 'Сохранить'
    TabOrder = 6
    OnClick = btSaveClick
  end
  object btLoad: TButton
    Left = 8
    Top = 168
    Width = 65
    Height = 25
    Caption = 'Загрузить'
    TabOrder = 7
    OnClick = btLoadClick
  end
  object btClear: TButton
    Left = 8
    Top = 200
    Width = 65
    Height = 25
    Caption = 'Очистить'
    TabOrder = 8
    OnClick = btClearClick
  end
  object od: TOpenDialog
    Filter = 'Файлы расшифровщика (*.jap)|*.jap|Файлы редактора (*.jdt)|*.jdt'
    Left = 56
    Top = 168
  end
  object sd: TSaveDialog
    Filter = 'Файлы расшифровщика (*.jap)|*.jap|Файлы редактора (*.jdt)|*.jdt'
    Left = 56
    Top = 136
  end
end
