Feature: Feature Sample

  Background: Background sample
    Given this is a sample 'background' step

  @tag1 @tag3
  Scenario Outline: Sample Scenario2
    Given this is a sample '<column1>' step
    When this is a sample '<column2>' step
    Then this is a sample '<column3>' step
    Examples:
      | column1 | column2 | column3 |
      | value1  | value2  | value3  |
      | value11 | value22 | value33 |