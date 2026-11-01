# MSgram

**MSgram is an unofficial Telegram client for Android.**

It is **not** made by, affiliated with, endorsed by, or in any way connected to
Telegram Messenger LLP. The names *Telegram*, the Telegram logo and the paper-plane
mark are trademarks of their respective owners and are not used as MSgram branding.

MSgram is a fork of the open-source
[Telegram for Android](https://github.com/DrKLO/Telegram) client. It connects to
Telegram's servers using the public [Telegram API](https://core.telegram.org/api)
with its **own `api_id`**, as the API terms require. Your account, your messages and
your data stay with Telegram — MSgram only changes how they are displayed on your
phone.

- Google Play: `uz.msgram.messenger`
- Official channel: [@MSgramUz](https://t.me/MSgramUz)
- Publisher: "MUZUFIXAS" MChJ (brand: Muallimi Soniy)

## License

MSgram is released under **GNU General Public License v2.0**, the same licence as
the upstream Telegram for Android source. See [LICENSE](LICENSE).

Because MSgram is distributed as a binary on Google Play, the complete corresponding
source code for every published version is kept in this repository, as GPL v2
section 3 requires.

## What MSgram changes

MSgram keeps the upstream messenger intact and adds a small set of features:

| Feature | Description |
|---|---|
| **StopVirus** | `.apk` files arriving in chats are never auto-downloaded, are deleted from storage, and the user is warned that the file may be malware. |
| **Administrator folder** | A local chat folder listing only the channels and groups where you are an administrator. It exists only on the device and is never sent to Telegram's servers. |
| **Up to 50 accounts** | The account limit is raised from 3 to 50 (both the Java and the native MTProto layer). |
| **Hide ads** | Sponsored messages in channels and sponsored search results can be turned off. |
| **MSgram tab** | An extra tab in the bottom bar with MSgram's own settings. |
| **Account deletion** | Settings → Privacy and Security → *Delete account permanently*, which calls `account.deleteAccount` and wipes the device. |
| **Branding** | Own name, icon, colour scheme, wordmark and package id. |

Everything else — the protocol, encryption, storage and UI — is upstream Telegram
code, unchanged.

## Building

```bash
git clone --recursive https://github.com/<owner>/MSgram.git
cd MSgram
./gradlew :TMessagesProj_App:bundleBundleAfatRelease
```

You need Android Studio with the NDK and CMake installed.

Signing credentials are **not** in this repository. To produce a signed build,
create `~/.gradle/gradle.properties` with your own values:

```properties
RELEASE_KEY_ALIAS=<your alias>
RELEASE_KEY_PASSWORD=<your password>
RELEASE_STORE_PASSWORD=<your password>
```

and place your own keystore at `TMessagesProj/config/release.keystore`.

To run MSgram against your own API credentials, replace `APP_ID` and `APP_HASH` in
`TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java` with an
[api_id you obtained yourself](https://core.telegram.org/api/obtaining_api_id).
Do not use MSgram's.

---

## O'zbekcha

**MSgram — Android uchun norasmiy Telegram mijozi.**

U Telegram Messenger LLP tomonidan ishlab chiqilmagan va u bilan hech qanday
aloqasi yo'q. MSgram Telegram'ning ochiq kodli Android ilovasidan forklangan va
Telegram'ning ochiq API'siga **o'zining `api_id`si** bilan ulanadi. Akkauntingiz,
xabarlaringiz va ma'lumotlaringiz Telegram serverlarida qoladi — MSgram faqat
ularni telefonda qanday ko'rsatishni o'zgartiradi.

Kod **GPL v2** litsenziyasi ostida, Telegram'ning asl kodi bilan bir xil. Play
Market'da chiqarilgan har bir versiyaning to'liq manba kodi shu repozitoriyda
turadi.

Rasmiy kanal: [@MSgramUz](https://t.me/MSgramUz)
