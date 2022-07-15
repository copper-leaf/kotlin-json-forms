---
---

# Usage Guide

This library aims to re-implement the same JSON Schema-based forms from the 
[Javascript JSON Forms library](https://jsonforms.io/docs). As such, much of the documentation from that site will be 
valid here, in particular the values of anything in JSON files. All the [examples](https://jsonforms.io/examples/basic)
from that site are also valid form definitions here, and you can see examples in action for Desktop and Android targets
[here](https://github.com/copper-leaf/kotlin-json-forms). Some specific widgets from those examples may not render the
same as not all UI widgets have been implemented yet, but these features are planned.

# Supported targets

Kotlin Json Forms currently supports Compose Material for Desktop and Android, but most of this project is built using
common Kotlin APIs which should be able to support other UI targets. In cases where platform-specific functionality or
dependencies are needed, they are implemented with `expect/actual` declarations to eventually aid in adopting other
targets, such as Compose for Web.

# Dependencies

This library is based on [Ballast](https://github.com/copper-leaf/ballast) for state management, and uses the the JSON
models and (de)serialization capabilities from [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization), 
though knowledge of either library is not necessary to use Kotlin Json Forms. 

Additionally, schema validation is supported individually for each platform, because schema validator libraries are 
large and too complex to reimplement for this project, so it's easier to find appropriate libraries on each platform as
needed, and use them through `expect/actual` declarations. 
[json-kotlin-schema](https://github.com/pwall567/json-kotlin-schema) is the library currently in use for Compose Desktop
and Android targets.

# Basic Usage

The simplest usage of this library looks like this:

```kotlin
@Composable
fun RenderFormPreview(
    formStore: FormSavedStateAdapter.Store
) {
    // create the Ballast ViewModel which manages the form State
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(coroutineScope, formStore) {
        BasicFormViewModel(
            coroutineScope,
            FormSavedStateAdapter(formStore)
        )
    }
    
    // render the Form UI
    Form(vm)
    
    // if you need to do anything else with the form state, you can collect the ViewModel State to observe any changes
    val vmState by vm.observeStates().collectAsState()
}
```

`FormSavedStateAdapter.Store` is an interface that must be implemented by you, and is how the Form schema gets 
populated, and where form state is saved and loaded from. All values returned from the Store must be JSON strings.

# Form State

Form data is managed with the MVI pattern with the 
[Ballast state management library](https://github.com/copper-leaf/ballast). The form schemas and the associated data is
managed centrally in a ViewModel class, and any changes to the state are sent through a `StateFlow` to be collected by
the UI and updated. Anytime any field of the form's data changes, the entire form will be updated accordingly, and all
rules will be re-evaluated. 

The Form ViewModel manages a `StateFlow` of `FormContract.State`, which holds onto a lot of useful data which may be 
needed by the form UI renderer, but may be useful for custom UI elements as well. In particular, it keeps a copy of the
original data separately from the data that's currently in the UI, and has properties available to let you know whether 
the data has been changed, or whether any individual control widget in the UI has been interacted with or its value 
changed.

The form's initial configuration is provided to the ViewModel with `FormSavedStateAdapter.Store`, which should be 
created by you, returning JSON Strings of the Schema and UI schema to the ViewModel. It also is the mechanism by which 
you will be notified when the form's data has changed. You can also change when the form state will be sent to the 
Store by configuring the `FormSavedStateAdapter`'s `SaveType` when you provide it to the ViewModel. The `SaveType` can 
be one of the following values:

- `SaveType.OnValidChange`: After any change is made to the form data, if the updated data is valid according to the 
  provided JSON Schema, then the form will be saved automatically.
- `SaveType.OnAnyChange`: After any change is made to the form data, then the form will be saved automatically, even if 
  it is not valid against the provided JSON Schema
- `SaveType.OnCommit`: The form will never save itself automatically, but will only be saved when the user explicitly 
  chooses to save the form with some button in the UI. Kotlin Json Forms has a built-in submit button which can be added 
  to the UI Schema definition with an element like this: `{ "type": "Button", "options": { "action": "submit" } }`, but 
  you are free to build your own custom UI element outside of the form which calls 
  `vm.trySend(FormContract.Inputs.CommitChanges)` to accomplish the same functionality.

# Supported Features

## UI Elements

UI Elements are defined in the UI schema, and control how the form is physically laid out and rendered on the page. The 
table below shows the different controls that are currently implemented or will be soon.

| UI Element                                                                                      | Element Type in UI Schema    | Other UI Schema config                   | Supported |
|-------------------------------------------------------------------------------------------------|------------------------------|------------------------------------------|-----------|
| Render child elements in a Compose `Column`                                                     | `"type": "VerticalLayout"`   |                                          | Yes       |
| Render child elements in a Compose `Row`                                                        | `"type": "HorizontalLayout"` |                                          | Yes       |
| Render a Control according to the referenced field type (see next section)                      | `"type": "Control"`          |                                          | Yes       |
| A simple Label                                                                                  | `"type": "Label"`            |                                          | Yes       |
| Group related elements in a card                                                                | `"type": "Group"`            |                                          | Yes       |
| Set up a tabbed view. Each tab will be shown/hidden according to the Rule of the element itself | `"type": "Categorization"`   |                                          | Yes       |
| One tab as a child of `Categorization`                                                          | `"type": "Category"`         |                                          | Yes       |
| A Submit button to commit form changes. Only visible when using `SaveType.OnCommit`             | `"type": "Button"`           | `"options": { "action": "submit" }`      | Yes       |
| A checkbox to show/hide debug info                                                              | `"type": "Button"`           | `"options": { "action": "toggleDebug" }` | Yes       |

## Controls

Controls are defined with an element in the UI Schema with `"type": "Control"` and a `scope` property that is a JSON 
Pointer to a field in the form Schema. The field type in the Schema defines the control widget that is rendered, but 
some field types may be rendered differently based on a combination of properties in both the Schema and the UI Schema.

There are several controls available in the original JavaScript library that are not available in Kotlin Json Forms yet, 
but a goal is to have all controls from that library fully working here soon. The table below shows the different 
controls that are currently implemented or will be soon.

| UI Control                                                | Field Type in Schema | Other Schema config                                         | Other UI Schema config              | Supported                             |
|-----------------------------------------------------------|----------------------|-------------------------------------------------------------|-------------------------------------|---------------------------------------|
| Normal text field                                         | `string`             |                                                             |                                     | Yes                                   |
| Rich text field editor                                    | `string`             |                                                             | `"options": { "richText": true }`   | Partial, doesn't actually update data |
| Code text field editor                                    | `string`             |                                                             | `"options": { "codeEditor": true }` | Yes                                   |
| Text field with dropdown menu                             | `string`             | `"enum": [...]`                                             |                                     | Partial, dropdown is clunky           |
| Text field with Radio Buttons                             | `string`             | `"enum": [...]`                                             | `"options": { "format": "radio" }`  | Yes                                   |
| Text field with dropdown menu                             | `string`             | `"oneOf": [{}, {}]`                                         |                                     | Partial, dropdown is clunky           |
| Text field with Radio Buttons                             | `string`             | `"oneOf": [{}, {}]`                                         | `"options": { "format": "radio" }`  | Yes                                   |
| Text field with Date Picker                               | `string`             | `"format": "date"`                                          |                                     | No                                    |
| Text field with Time Picker                               | `string`             | `"format": "time"`                                          |                                     | No                                    |
| Text field with DateTime Picker                           | `string`             | `"format": "datetime"`                                      |                                     | No                                    |
| Text field with up/down buttons                           | `integer`            |                                                             |                                     | Yes                                   |
| Text field with up/down buttons                           | `number`             |                                                             |                                     | Yes                                   |
| Checkbox                                                  | `boolean`            |                                                             |                                     | Yes                                   |
| Switch                                                    | `boolean`            |                                                             | `"options": { "toggle": true }`     | Yes                                   |
| Multi-select Checkboxes                                   | `array`              | `uniqueItems: true, items: {"type": "string", "enum": [] }` |                                     | Yes                                   |
| Multi-select Checkboxes                                   | `array`              | `uniqueItems: true, items: {"oneOf": [{}, {}] }`            |                                     | Yes                                   |
| Render all properties of the object as fields             | `object`             |                                                             |                                     | Yes                                   |
| Add/remove items from the array (including nested arrays) | `array`              |                                                             |                                     | Partial, UI is ugly                   |

## Validation

Each control rendered in the UI will check for validation error messages and display them in the UI. JSON Schema 
validation is very complex, and the messaged displayed in the UI come directly from the platform's validation library, 
which may not be the most user-friendly. At this time, it is not a goal to provide more user-friendly or localized 
error messages.

# Roadmap

This is a list of features planned for this library. None of them are guaranteed, all of them are open for contribution.

- Improve rich-text editor
  - Contribute improvements to the upstream library for [richtext-compose-multiplatform](https://github.com/Wavesonics/richtext-compose-multiplatform)
  - Improve toolbar and keyboard shortcuts
  - Support saving/restoring rich text field state to/from HTML and Markdown
  - Allow the user to switch between the rich-text editor, and editing the markup directly
  - When editing markup directly, allow user to choose whether to show a rendered preview of the rich text
- Add code editor widget
  - Potentially based on [this library](https://github.com/Qawaz/compose-code-editor), if it would move its dependencies to MavenCentral
- Improve UX of dropdown-based widgets
- Support date/time pickers
  - Ideally, these would use the native picker dialogs on Android, and custom-built ones for Desktop
- Add UI elements to display refs to properties, or computed expressions
  - Use [trellis](https://github.com/copper-leaf/trellis) for building the expression DSL here
- Improve UX of array widgets
  - would be nice to have list, master/detail, and table views for array-of-object controls
- Improve error messages
  - See if there's a way to map the error messages of the validator libs to common strings (especially ones that could be localized)
  - Add a way to provide custom additional error messages (such as coming from an API after submitting the form)
- Support `readonly` flag
  - Globally on the entire form (set via FormContract.State)
  - On any individual Control
  - On a property within the JSON Schema
- Support `definitions` so that object/array controls can use `oneOf`, `anyOf`, or `allOf` with `$ref` to the definitions
- Automatically move focus between controls with tab on desktop
