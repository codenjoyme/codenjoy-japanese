object Form1: TForm1
  Left = 245
  Top = 144
  BorderIcons = [biSystemMenu, biMinimize]
  BorderStyle = bsSingle
  Caption = 'Японские головоломки'
  ClientHeight = 242
  ClientWidth = 574
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poScreenCenter
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object pb: TPaintBox
    Left = 80
    Top = 8
    Width = 489
    Height = 227
    OnMouseDown = pbMouseDown
    OnMouseMove = pbMouseMove
    OnMouseUp = pbMouseUp
    OnPaint = pbPaint
  end
  object edCountX: TEdit
    Left = 4
    Top = 8
    Width = 44
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    ReadOnly = True
    TabOrder = 1
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountX: TUpDown
    Left = 48
    Top = 8
    Width = 12
    Height = 21
    Associate = edCountX
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 2
    Wrap = False
  end
  object btCalc: TButton
    Left = 8
    Top = 88
    Width = 65
    Height = 25
    Caption = '&Расчет'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 6
    OnClick = btCalcClick
  end
  object cbMode: TCheckBox
    Left = 8
    Top = 64
    Width = 67
    Height = 17
    Caption = 'Расш.'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 5
    OnClick = cbModeClick
  end
  object edCountY: TEdit
    Left = 4
    Top = 32
    Width = 44
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    ReadOnly = True
    TabOrder = 3
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountY: TUpDown
    Left = 48
    Top = 32
    Width = 12
    Height = 21
    Associate = edCountY
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 4
    Wrap = False
  end
  object btSave: TButton
    Left = 8
    Top = 113
    Width = 65
    Height = 25
    Caption = '&Сохранить'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 7
    OnClick = btSaveClick
  end
  object btLoad: TButton
    Left = 8
    Top = 138
    Width = 65
    Height = 25
    Caption = '&Загрузить'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 8
    OnClick = btLoadClick
  end
  object btClear: TButton
    Left = 8
    Top = 163
    Width = 65
    Height = 25
    Caption = '&Очистить'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 9
    OnClick = btClearClick
  end
  object edInput: TEdit
    Left = 8
    Top = 216
    Width = 65
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 0
    OnKeyPress = edInputKeyPress
    OnMouseDown = edInputMouseDown
  end
  object cbRjad: TCheckBox
    Left = 64
    Top = 24
    Width = 14
    Height = 17
    Caption = 'cbRjad'
    TabOrder = 11
    Visible = False
    OnClick = cbRjadClick
  end
  object btSaveBimap: TButton
    Left = 8
    Top = 188
    Width = 65
    Height = 25
    Caption = 'Р&исунок'
    TabOrder = 10
    OnClick = btSaveBimapClick
  end
  object od: TOpenDialog
    Filter = 'Файлы расшифровщика (*.jap)|*.jap|Файлы редактора (*.jdt)|*.jdt'
    Left = 56
    Top = 136
  end
  object sd: TSaveDialog
    Filter = 'Файлы расшифровщика (*.jap)|*.jap|Файлы редактора (*.jdt)|*.jdt'
    Left = 56
    Top = 112
  end
  object spd: TSavePictureDialog
    Filter = 'Bitmaps (*.bmp)|*.bmp'
    Left = 56
    Top = 184
  end
end
