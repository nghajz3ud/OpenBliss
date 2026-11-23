# BlissPlugin — Credits

## Base Skript: rar

This Java plugin is a port and expansion of the original **Bliss SMP Skript**
written by **rar**. All core mechanics — the gem system, pedestal rituals,
bottled-energy / repair-kit flow, the trader/upgrader, the eight base gems
(Strength, Fire, Wealth, Speed, Puff, Astra, Life, Flux) and the
foundational damage/progression rules — derive from rar's Skript source.

rar explicitly granted permission to port, modify, publish, and commercially
distribute the work, on the condition that his Skript is credited as the
base. See `permission.png` in this same `credits/` folder for the full
exchange (Discord conversation, 9:06 PM → 9:10 PM).

His exact words:

> "Hey, I'm honestly totally fine with that.
> As long as my skript mentioned at least somewhere as the base for your
> plugin or anything similar to that, you can do whatever with it.
> Go ahead if you want to sell it, too, just don't scam people and you'll
> be just fine."

## What this plugin adds on top of rar's Skript

- Full Java rewrite (Paper 1.21.4 API) — no Skript runtime required.
- **Bliss Villager Event** — entirely new event (compass / villager / token
  / mythic-soul flow) authored by Blood.
- **Mythic gems** — Heretic and Auratus integrations beyond rar's base set.
- **Ultimate Trader / Upgrader** — finished from the unfinished portion in
  rar's source.
- Bug fixes throughout the original Skript (notably Flux beam scaling,
  Flux Ground damage ordering, Wealth Misfortune, pedestal state edge
  cases, deposit-cooldown gating, and more).
- Citizens integration for Speed Blur dash NPCs.

## How to view the permission screenshot

`permission.png` ships inside this JAR under `credits/permission.png`.
Extract the JAR with any unzip tool (it's a standard zip):

```
unzip BlissPlugin.jar -d BlissPlugin-extracted
open BlissPlugin-extracted/credits/permission.png
```
