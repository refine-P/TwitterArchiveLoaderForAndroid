# Background
自分の鍵垢のツイートを検索したい場合は、Twitterのアーカイブをダウンロードして使うと良い。  
Twitterのアーカイブはブラウザ上だと良い感じのUIで閲覧できる ([参照](https://note.com/mome_ka_mome/n/n38c94915e0aa))。  
ただ、スマホだと個人的に扱いづらい感じがしたので、簡易的なAndroidのアプリを作成した。  

# Quick start
1. https://help.x.com/ja/managing-your-account/how-to-download-your-x-archive を参考にTwitterのアーカイブをダウンロードする。
2. ダウンロードしたTwitterのアーカイブの `data/tweets.js` をローカルやGoogle Drive等のスマホからアクセス可能な場所に保存しておく。（その際、ファイルの名前は変更しておいた方が良い。）
3. https://github.com/refine-P/TwitterArchiveLoaderForAndroid/releases/latest/app-debug.apk からアプリをダウンロード。
4. アプリを開いて、`Settings` のタブから2.で用意したファイルを読み込む。
5. `Home` のタブからアーカイブの閲覧、`Search` のタブからアーカイブの検索ができる。

# Reference
* https://note.com/merupon/n/n523a770e13a5 を参考に作り始めた。
