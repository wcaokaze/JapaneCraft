
![Screenshot](screenshot.png)


設定
--------------------------------------------------------------------------------
### JapaneCraft.cfg

- format.S:chat

    チャットの表示フォーマット。

    | 変数                  | 概要                                              |
    | :-------------------- | :------------------------------------------------ |
    | `$username`           | 発言者の名前。                                    |
    | `$time`               | 発言した時間。                                    |
    | `$rawMessage`         | 発言内容。`$convertedMessage`と一致する場合は空。 |
    | `$convertedMessage`   | 日本語に変換された文字列。                        |
    | `$n`                  | 改行コード。U+000a。                              |
    | `$$`                  | '$'。 U+0024。                                    |

    デフォルト値は`<$username> $rawMessage$n  §b$convertedMessage`。

- format.S:time

    時刻の表示フォーマット。`java.text.SimpleDateFormat`参照。

- mode.B:enableConvertingToKanji

    漢字への変換を利用するかどうか。

### JapaneCraftRomajiTable.json

ローマ字テーブル。

### JapaneCraftDictionary.json

辞書。漢字変換がうまくいかないときにご活用ください。

License
--------------------------------------------------------------------------------
MIT. See [LICENSE](LICENSE).

