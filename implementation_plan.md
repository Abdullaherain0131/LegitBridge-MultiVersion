# True Scaffold Implementation

This plan introduces a "True Scaffold" feature that allows the user to look in any direction while the mod sends spoofed rotation packets to the server to place blocks below them, creating a straight path.

## User Review Required

> [!IMPORTANT]  
> The current `LegitScaffold` module acts as a "NinjaBridge" (auto-sneaks at block edges). Do you want to **replace** the existing `LegitScaffold` with this new True Scaffold feature, or should we create a completely separate module called `Scaffold` (leaving `LegitScaffold` as is)?

## Open Questions

> [!WARNING]
> How aggressive should the packet spoofing be? Should it snap instantly to the block, or smoothly interpolate to look "legit" to the anti-cheat? The plan assumes instant snapping for the server packets, which may flag strict anti-cheats (like Vulcan) unless we add some smoothing.

## Proposed Changes

### Scaffold Module

#### [NEW] `src/main/java/com/ersin/legitbridge/module/impl/Scaffold.java`
- Will calculate the target block position (usually `player.getBlockPos().down()`).
- Searches for an adjacent solid block to attach to.
- Calculates the required yaw and pitch to look at the target block face.
- Intercepts `ClientConnection.send` (via `MixinClientConnection`) to spoof `PlayerMoveC2SPacket` with the calculated yaw/pitch, just like the `Derp` module does.
- If `client.options.keyUse.isPressed()` (Right-Click is held), it manually sends a `PlayerInteractBlockC2SPacket` and a Hand Swing packet to place the block, bypassing the client's actual crosshair.

### Mixin Changes

#### [MODIFY] `src/main/java/com/ersin/legitbridge/mixin/MixinClientConnection.java`
- Add an injection point for the new `Scaffold` module (similar to `Derp` and `FakeLag`) to allow it to intercept and replace `PlayerMoveC2SPacket` with spoofed rotations.

## Verification Plan

### Automated Tests
- None.

### Manual Verification
- Compile for 1.16.5 and place in `mods/`.
- Look up at the sky and hold right-click while walking forward.
- Verify that blocks are placed below the player.
- Ensure that the camera on the client side does not snap downwards (the player can still look around freely).
