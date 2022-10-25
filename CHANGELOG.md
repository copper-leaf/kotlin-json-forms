## 0.7.0 - 2022-10-25

- Updates to Kotlin 1.7.20
- Updates to Compose 1.2.0
- Temporarily removes Compose Web and Bulma as targets, because Compose Web does not work with Kotlin 1.7.20

## 0.6.0 - 2022-08-26

- Significant refactor, removes Ballast as a dependency to use normal Compose state management without enforcing any
  particular pattern

## 0.5.0 - 2022-08-19

- New `json-forms-compose-core` artifact has been added with all the base functionality using only Compose Runtime
- Added `json-forms-compose-bulma` to display forms in Compose/Web with Bulma styling, based on `json-forms-compose-core` 
- `json-forms-compose` has been renamed to `json-forms-compose-material`, which is also based on `json-forms-compose-core`

## 0.4.0 - 2022-07-16

- Use already-parsed JsonElement instead of forcing the library to only work with JSON Strings

## 0.3.0 - 2022-07-15

- Adds code editor control
- Connects rich text widget to form state
- adds multi-select checkbox controls

## 0.2.0 - 2022-07-15

- Adds a few more widget types
- Slightly improves UI

## 0.1.0 - 2022-07-05

- Initial Commit
