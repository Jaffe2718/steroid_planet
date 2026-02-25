# Steroid Planet

![icon](common/src/main/resources/assets/steroid_planet/icon.png)

![Stars](https://img.shields.io/github/stars/Jaffe2718/steroid_planet?style=flat-square)
![Forks](https://img.shields.io/github/forks/Jaffe2718/steroid_planet?style=flat-square)
![Issues](https://img.shields.io/github/issues/Jaffe2718/steroid_planet?style=flat-square)
![Licence](https://img.shields.io/github/license/Jaffe2718/steroid_planet?style=flat-square)

## Introduction

Steroid Planet adds a high-stakes bodybuilding system to Minecraft! Unlock tiered steroids (I/II/III) to boost muscle
mass, reduce body fat, and chase extreme milestones like "Fat Loss Master" (≤10% body fat) or "Steroid Master" (use 9
types of steroid). Track critical stats—muscle, liver health, and body fat—with risky tradeoffs: steroids grant power
but risk permanent liver damage. Earn challenge-tier advancements, unique rewards, and face the consequences of reckless
fitness pursuit in this adrenaline-fueled progression mod.

## Features

### Effects

| Effect       | Icon                                                                                      | ID                            | Translation Key                      | Description                                                                                                                                                                                  |
|--------------|-------------------------------------------------------------------------------------------|-------------------------------|--------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contest Prep | ![](common/src/main/resources/assets/steroid_planet/textures/mob_effect/contest_prep.png) | `steroid_planet:contest_prep` | `effect.steroid_planet.contest_prep` | There is absolutely no `Muscle` loss during contest preparation                                                                                                                              |
| Tech Fitness | ![](common/src/main/resources/assets/steroid_planet/textures/mob_effect/tech_fitness.png) | `steroid_planet:tech_fitness` | `effect.steroid_planet.tech_fitness` | There are `I`, `II`, `III` levels, each level has different effects. Tech fitness can increase the efficiency of `Muscle` building and `BodyFat` loss, while causing damage to `LiverHealth` |

### Player Attributes

| Attribute    | Type  | NBT Key       | Default Value | Description                                                                                                                                                                                                                                                                                                                                                                                                                     |
|--------------|-------|---------------|---------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Body Fat     | Float | `BodyFat`     | `30.0F`       | Body fat is valued from `0.0FF` to `100.0F`, and body fat stops muscle growth because the sum of `Muscle` and `BodyFat` is `100.0F`. Eating foods with labels `steroid_planet:fat_i`, `steroid_planet:fat_ii`, or `steroid_planet:fat_iii` can increase body fat. Players need to lose fat by running and swimming, and `steroid_planet:tech_fitness` effect can accelerate fat loss.                                           |
| Liver Health | Float | `LiverHealth` | `100.0F`      | Liver health values are taken from `0.0F` to `100.0F`. When the `LiverHealth` value is below `15.0F`, it will continue to be harmed by `steroid_planet:liver_poisoning`, and the lower the liver health, the higher the damage multiplier. Players can restore liver health by sleeping and eating foods with tag `#steroid_planet:liver_healing_i`, `#steroid_planet:liver_healing_ii` or `#steroid_planet:liver_healing_iii`. |
| Muscle       | Float | `Muscle`      | `0.0F`        | Muscle is valued from `0.0F` to `100.0F`, and the `Muscle` value can increase digging speed and attack power. Players can gain `Muscle` through exercise, including digging blocks and fighting, as well as eating foods with `steroid_planet:protein` tags. Having a `steroid_planet:tech_fitness` effect will increase the efficiency of diet and exercise.                                                                   |

### Damage Types

| Damage Type     | ID                               | Death Message                                                                              | Description |
|-----------------|----------------------------------|--------------------------------------------------------------------------------------------|-------------|
| Liver Poisoning | `steroid_planet:liver_poisoning` | RIP `YOUR NAME`. Abused too many vet steroids and moved to the steroid planet permanently. |             |

### Items

| Item                         | ID                                           | Texture                                                                                            | Translation Key                                   | Description |
|------------------------------|----------------------------------------------|----------------------------------------------------------------------------------------------------|---------------------------------------------------|-------------|
| Bodybuilding Champion Trophy | `steroid_planet:champion_trophy`             | ![](common/src/main/resources/assets/steroid_planet/textures/item/champion_trophy.png)             | `item.steroid_planet.champion_trophy`             |             |
| CIS Trenbolone               | `steroid_planet:cis_trenbolone`              | ![](common/src/main/resources/assets/steroid_planet/textures/item/cis_trenbolone.png)              | `item.steroid_planet.cis_trenbolone`              |             |
| Methenolone                  | `steroid_planet:methenolone`                 | ![](common/src/main/resources/assets/steroid_planet/textures/item/methenolone.png)                 | `item.steroid_planet.methenolone`                 |             |
| Nandrolone                   | `steroid_planet:nandrolone`                  | ![](common/src/main/resources/assets/steroid_planet/textures/item/nandrolone.png)                  | `item.steroid_planet.nandrolone`                  |             |
| Nandrolone Decanoate         | `steroid_planet:nandrolone_decanoate`        | ![](common/src/main/resources/assets/steroid_planet/textures/item/nandrolone_decanoate.png)        | `item.steroid_planet.nandrolone_decanoate`        |             |
| Nandrolone Phenylpropionate  | `steroid_planet:nandrolone_phenylpropionate` | ![](common/src/main/resources/assets/steroid_planet/textures/item/nandrolone_phenylpropionate.png) | `item.steroid_planet.nandrolone_phenylpropionate` |             |
| Oxandrolone                  | `steroid_planet:oxandrolone`                 | ![](common/src/main/resources/assets/steroid_planet/textures/item/oxandrolone.png)                 | `item.steroid_planet.oxandrolone`                 |             |
| Oxymetholone                 | `steroid_planet:oxymetholone`                | ![](common/src/main/resources/assets/steroid_planet/textures/item/oxymetholone.png)                | `item.steroid_planet.oxymetholone`                |             |
| Stanozolol                   | `steroid_planet:stanozolol`                  | ![](common/src/main/resources/assets/steroid_planet/textures/item/stanozolol.png)                  | `item.steroid_planet.stanozolol`                  |             |
| Stenbolone                   | `steroid_planet:stenbolone`                  | ![](common/src/main/resources/assets/steroid_planet/textures/item/stenbolone.png)                  | `item.steroid_planet.stenbolone`                  |             |
| Synthol                      | `steroid_planet:synthol`                     | ![](common/src/main/resources/assets/steroid_planet/textures/item/synthol.png)                     | `item.steroid_planet.synthol`                     |             |
| Trenbolone                   | `steroid_planet:trenbolone`                  | ![](common/src/main/resources/assets/steroid_planet/textures/item/trenbolone.png)                  | `item.steroid_planet.trenbolone`                  |             |
| Trenbolone Acetate           | `steroid_planet:trenbolone_acetate`          | ![](common/src/main/resources/assets/steroid_planet/textures/item/trenbolone_acetate.png)          | `item.steroid_planet.trenbolone_acetate`          |             |
| Trestolone                   | `steroid_planet:trestolone`                  | ![](common/src/main/resources/assets/steroid_planet/textures/item/trestolone.png)                  | `item.steroid_planet.trestolone`                  |             |

### Advancement

| Advancement            | Icon Item ID                     | ID                                               | Title Translation Key                                               | Description |
|------------------------|----------------------------------|--------------------------------------------------|---------------------------------------------------------------------|-------------|
| Bodybuilding Champion  | `steroid_planet:champion_trophy` | `steroid_planet:adventure/bodybuilding_champion` | `advancements.steroid_planet.adventure.bodybuilding_champion.title` |             |
| Buff on Buff           | `steroid_planet:oxymetholone`    | `steroid_planet:adventure/buff_on_buff`          | `advancements.steroid_planet.adventure.buff_on_buff.title`          |             |
| Fat Loss Master        | `minecraft:cake`                 | `steroid_planet:adventure/fat_loss_master`       | `advancements.steroid_planet.adventure.fat_loss_master.title`       |             |
| Pointy Head            | `minecraft:pointed_dripstone`    | `steroid_planet:adventure/pointy_head`           | `advancements.steroid_planet.adventure.pointy_head.title`           |             |
| Steroid Master         | `minecraft:dragon_head`          | `steroid_planet:adventure/steroid_master`        | `advancements.steroid_planet.adventure.steroid_master.title`        |             |
| Newbie's First Steroid | `steroid_planet:stanozolol`      | `steroid_planet:adventure/tech_fitness_i`        | `advancements.steroid_planet.adventure.tech_fitness_i.title`        |             |
| Noob to Hunk           | `steroid_planet:cis_trenbolone`  | `steroid_planet:adventure/tech_fitness_ii`       | `advancements.steroid_planet.adventure.tech_fitness_ii.title`       |             |
| Steroid Hero           | `steroid_planet:trestolone`      | `steroid_planet:adventure/tech_fitness_iii`      | `advancements.steroid_planet.adventure.tech_fitness_iii.title`      |             |

## License

[MIT License](LICENSE)

## Links

[Repository](https://github.com/Jaffe2718/steroid_planet)
[Releases](https://github.com/Jaffe2718/steroid_planet/releases)
[Issues](https://github.com/Jaffe2718/steroid_planet/issues)
[Modrinth](https://modrinth.com/mod/steroid_planet)
