# Fishing Spot Tracker

A [RuneLite](https://runelite.net/) plugin that displays a **pie-timer circle** over fishing spots. The circle depletes and shifts color as the spot ages, giving you a visual countdown of when it's likely to move.

![icon](icon.png)

## Features

- **Pie-timer circle** that visually depletes from full to empty as the spot approaches its move window
- **Color gradient** shifts from fresh (cyan) to expiring (red) — both colors are fully customizable
- **Active fishing indicator** — circle turns green when you're fishing at a spot
- **Newest spot badge** — gold star marks the most recently spawned spot (least likely to move)
- **Timer text** showing elapsed time per spot
- **Timer persistence** — remembers spot timers when you walk out of range and restores them when you return
- **Per-spot-type tick ranges** — accurate timers for standard spots, aerial fishing, minnows, sacred/infernal eels, anglerfish, and more
- **Idle notification** — alerts you when you stop fishing
- **Fish sprite icons** — optional item sprite rendered on each spot
- **Spot deduplication** — multiple NPCs on the same tile only render one overlay

## Configuration

### Colors
| Option | Default | Description |
|--------|---------|-------------|
| Fresh Color | Cyan | Circle color when a spot has just appeared |
| Expiring Color | Red | Circle color when a spot is about to move |
| Active Fishing Color | Green | Circle color when you are fishing at a spot |
| Circle Opacity | 128 | Overlay transparency (0-255) |

### Display
| Option | Default | Description |
|--------|---------|-------------|
| Show Circle | On | Toggle the pie-timer circle overlay |
| Circle Radius | 18 | Size of the circle in pixels |
| Stroke Width | 2 | Outline thickness |
| Fill Circle | On | Fill the circle with a translucent color |
| Show Timer Text | On | Elapsed time label above each spot |
| Show Spot Name | Off | Fish name label below each spot |
| Show Fish Icon | Off | Item sprite on each spot |
| Show Newest Spot Badge | On | Gold star on the newest spot |

## Building

Requires **JDK 11**.

```bash
./gradlew compileJava    # compile
./gradlew run            # launch RuneLite with the plugin loaded
```

## License

BSD 2-Clause — see [LICENSE](LICENSE).
