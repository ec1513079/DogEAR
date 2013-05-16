【必要要素】
・OpenSSH
・git-client
・make
・android-ndk
・android-sdk

git@gitlab.mobilegate.biz:kotakahashi/scaforandroid.git からPull

mupdfを取得
$ git submodule update --init

まずはmupdfライブラリをセットアップ

mupdfのサブモジュールを取得
$ cd mupdf
/mupdf $ git submodule update --init

makeでビルドに必要なディレクトリとファイルを作成
/mupdf $ make generate

ndkビルド
（ターミナルでもできるが、最近のADT（ADTr20）を使えば、
　CDT (C/C++ Development Tools) がついてくるので、Eclipse上でもビルドできる）

：ターミナルでビルド
/mupdf $ cd android
/mupdf/android $ cp local.properties.sample local.properties
/mupdf/android $ ndk-build

：Eclipseでビルド
Eclipseで/mupdf/android のプロジェクトを開き
プロジェクトを右クリック - [Android ツール] - [Add Native Support ...] を実行して、
プロジェクトに NDK をサポートさせる。
自動ビルドONならそのままビルドされるはず。

※ Cファイルのインクルードでエラーが出る場合
プロジェクトのプロパティ - C/C++ 一般 ‐ パスおよびシンボル - インクルード で、
"C:\android-ndk\platforms\android-14\arch-arm\usr\include"
"C:\Users\kotakahashi\Documents\workspace\mupdf\fitz"
をインクルードパスに追加。

【SCAforAndroidをビルド】

まずはmupdfをライブラリとして登録する。

1. mupdfのandroidプロジェクトのプロパティ - Android - ライブラリパネルの
　　「ライブラリ」チェックボックスをON
2. SCAプロジェクトのプロパティ - Android - ライブラリパネルで
　　mupdfを参照ライブラリとして登録。

これでビルドが通るはず。