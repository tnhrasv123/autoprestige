# Prestige Automation Mod

A Fabric client-side mod that automates prestige and ascension mechanics in Minecraft using the server's `/prestige` and `/ascension` commands.

## Features

- **Prestige Automation**: Automatically clicks prestige items when requirements are met
- **Ascension Automation**: Handles the complex ascension process with roman numeral parsing
- **Configurable Timers**: Set custom intervals for each automation type
- **Smart Inventory Scanning**: Parses item lore with number formatting support
- **Safe Click Simulation**: Prevents spam clicking with configurable delays
- **Debug Mode**: Detailed logging for troubleshooting
- **Command Interface**: Easy configuration through in-game commands

## Commands

All commands use the `/autorank` prefix with subcommands for prestige and ascension:

### Prestige Commands
- `/autorank prestige toggle` - Enable/disable prestige automation
- `/autorank prestige interval <ticks>` - Set prestige check interval
- `/autorank prestige status` - Show current prestige settings
- `/autorank prestige slot <0-8>` - Set inventory slot to monitor
- `/autorank prestige debug` - Toggle debug mode

### Ascension Commands
- `/autorank ascension toggle` - Enable/disable ascension automation
- `/autorank ascension interval <ticks>` - Set ascension check interval
- `/autorank ascension status` - Show current ascension settings

### General Commands
- `/autorank help` - Show all available commands

## Configuration

The mod creates a configuration file at `.minecraft/config/prestige-automation.json` with the following options:

```json
{
  "prestigeEnabled": false,
  "ascensionEnabled": false,
  "prestigeInterval": 100,
  "ascensionInterval": 200,
  "inventorySlot": 4,
  "debugMode": false,
  "clickDelay": 20
}
```

## How It Works

### Prestige Automation
1. Scans the configured inventory slot every interval
2. Parses item lore for patterns like:
   - "2,842 Rolls" (required value)
   - "(You have 3,500)" (current value)
3. Compares current >= required
4. Safely clicks the item if ready

### Ascension Automation
1. Extracts roman numeral from item name (e.g., "Prestige IV")
2. Calculates target prestige (current - 1)
3. Executes server `/prestige` command
4. Executes server `/ascension` command
5. Monitors for readiness using same lore parsing
6. Clicks when current value >= target prestige

## Number Parsing
- Handles comma-separated numbers (e.g., "1,234,567")
- Extracts longest number sequences from text
- Supports large numbers with proper formatting
- Strips Minecraft color/formatting codes

## Safety Features
- Configurable click delays prevent spam
- Error handling for missing items/invalid formats
- Debug mode for troubleshooting
- Automatic state reset on errors

## Installation

1. Install Fabric Loader for Minecraft 1.20.1
2. Download Fabric API
3. Place this mod in your mods folder
4. Launch Minecraft with Fabric profile

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.14.21+
- Fabric API 0.87.0+
- Java 17+

## Building

```bash
./gradlew build
```

The built mod will be in `build/libs/`.

## Example Usage

```
/autorank prestige toggle          # Enable prestige automation
/autorank prestige interval 60     # Check every 3 seconds
/autorank prestige slot 4          # Monitor middle inventory slot
/autorank ascension toggle         # Enable ascension automation
/autorank help                     # Show all commands
```