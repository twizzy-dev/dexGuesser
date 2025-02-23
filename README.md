# dexGuesser - v0.1.0

A simple command line Java application that generates a random integer, and you guess the Pokémon that shares that National Pokédex number.
Includes all Pokémon from gen 1 to gen 9.
Application keeps generating numbers until user tells it to stop (by typing `exit`).

```
Pokédex No. 25:
> pikachu
Correct, the Pokémon was Pikachu.
```

```
Pokédex No. 445:
> Weavile
The correct Pokémon is Garchomp, you were 16 off.
```

<br>

## Usage

Either:

- Download source code and export project to `.jar` file, and run in terminal with `java -jar <file-name>.jar`

or

- Clone repository to an IDE and run `CMDApplication` in the built-in terminal/console.

<br>

## pokedex.csv

List of all 1025 Pokémon currently in the [National Pokédex](https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_National_Pok%C3%A9dex_number) (from Bulbasaur to Pecharunt).

Information about a Pokémon is stored as follows:
- National Pokédex Number
- Pokémon's Name
- Primary Typing
- Secondary Typing (if applicable)
- Base Stat Total
- Base Stats
- Origin Game
- Form(s) (if applicable)
- Ability 1
- Ability 2 (if applicable)
- Hidden Ability (if applicable)
- Moveset
- Image (Bulbapedia Hyperlink)
- Status (Grouping i.e. Legendary, Starter, etc.)

Currently, the text file only contains the first 2 columns.
Other columns will be populated eventually (most likely using [PokeAPI.co](https://pokeapi.co/)).
`CMDApplication` only requires National Pokédex number and Pokémon name to function.
Extra columns exist for future functionality of application.