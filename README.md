# FirstJoinSpawn

## TR

Folia uyumlu bir Minecraft eklentisidir. Oyuncu sunucuya **ilk kez** katıldığında, ayarlanan gecikmeden sonra belirlenen spawn noktasına ışınlanır. Oyuncu öldüğünde yatak/anchor gibi bir yeniden doğma noktası yoksa, istenirse otomatik olarak aynı spawn noktasına yönlendirilir.

### Ozellikler

- Folia destekli (`folia-supported: true`)
- Ilk girislerde gecikmeli spawn isinlama
- Yatak/anchor yoksa otomatik spawn respawn yonlendirmesi
- Tum mesajlar ve davranislar `config.yml` uzerinden ayarlanabilir
- Tek komut agaci: `/fjs`

### Komutlar

- `/fjs setspawn` -> Mevcut konumu spawn olarak kaydeder
- `/fjs spawn` -> Oyuncuyu ayarli spawna isinlar
- `/fjs reload` -> `config.yml` dosyasini yeniden yukler

### Yetki

- `fjs.admin` (varsayilan: `op`)

### Kurulum

1. `target/FirstJoinSpawn-1.0.0.jar` dosyasini sunucunun `plugins` klasorune at.
2. Sunucuyu baslat.
3. Oyunda OP hesapla `/fjs setspawn` kullan.
4. `plugins/FirstJoinSpawn/config.yml` dosyasindan ayarlari duzenle.
5. Gerekirse `/fjs reload` calistir.

### Derleme (Maven)

```bash
mvn clean package
```

Olusan dosya:

```text
target/FirstJoinSpawn-1.0.0.jar
```

### Config Ozeti

- `spawn.*` -> Spawn konumu
- `first-join.enabled` -> Ilk giris ozelligi acik/kapali
- `first-join.teleport-delay-seconds` -> Ilk giriste isinlama gecikmesi
- `respawn.redirect-no-bed-to-spawn` -> Yatak/anchor yoksa spawna yonlendir
- `messages.*` -> Tum plugin mesajlari

---

## EN

A Folia-compatible Minecraft plugin. When a player joins the server **for the first time**, they are teleported to the configured spawn after a configurable delay. If a player dies and has no valid bed/anchor respawn point, they can be redirected to that same spawn automatically.

### Features

- Folia support (`folia-supported: true`)
- Delayed teleport on first join
- Optional respawn redirect to spawn when no bed/anchor exists
- All behavior and messages configurable via `config.yml`
- Single command root: `/fjs`

### Commands

- `/fjs setspawn` -> Saves your current location as spawn
- `/fjs spawn` -> Teleports the player to configured spawn
- `/fjs reload` -> Reloads `config.yml`

### Permission

- `fjs.admin` (default: `op`)

### Installation

1. Put `target/FirstJoinSpawn-1.0.0.jar` into your server `plugins` folder.
2. Start the server.
3. Use `/fjs setspawn` as an OP player.
4. Edit settings in `plugins/FirstJoinSpawn/config.yml`.
5. Run `/fjs reload` if needed.

### Build (Maven)

```bash
mvn clean package
```

Output:

```text
target/FirstJoinSpawn-1.0.0.jar
```

### Config Summary

- `spawn.*` -> Spawn location values
- `first-join.enabled` -> Enable/disable first join logic
- `first-join.teleport-delay-seconds` -> Delay before first-join teleport
- `respawn.redirect-no-bed-to-spawn` -> Redirect to spawn if no bed/anchor spawn exists
- `messages.*` -> All plugin messages
