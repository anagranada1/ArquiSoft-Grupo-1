Feature: Weather API acceptance tests

  @create-weather-ok
  Scenario: Create weather record and then search by country and city
    # Arrange: payload for a new weather record
    Given url baseUrl + '/api/v1/clima'
    And request
      """
      {
        "pais": "Colombia",
        "ciudad": "Medellín",
        "temperatura": 23.5,
        "sensacionTermica": 24.0,
        "humedad": 65,
        "descripcion": "Parcialmente nublado",
        "velocidadViento": 3.2
      }
      """
    # Act: POST to create
    When method post
    # Assert: 201 and body contains created data and HATEOAS links
    Then status 201
    And match response.pais == 'Colombia'
    And match response.ciudad == 'Medellín'
    And match response.temperatura == 23.5
    And match response._links.self.href != null
    And match response._links['crear-nuevo'].href != null

  @create-400-country
  Scenario: Create weather returns 400 when country is missing
    # Arrange: payload without country
    Given url baseUrl + '/api/v1/clima'
    And request
      """
      {
        "ciudad": "Bogotá",
        "temperatura": 15.0
      }
      """
    # Act: POST
    When method post
    # Assert: 400 and error message
    Then status 400
    And match response == { error: 'País y ciudad son obligatorios' }

  @create-400-city
  Scenario: Create weather returns 400 when city is missing
    # Arrange: payload without city
    Given url baseUrl + '/api/v1/clima'
    And request
      """
      {
        "pais": "Colombia",
        "temperatura": 15.0
      }
      """
    # Act: POST
    When method post
    # Assert: 400
    Then status 400
    And match response == { error: 'País y ciudad son obligatorios' }

  @search-weather
  Scenario: Search weather by country and city returns 200 and array
    # Arrange: known base URL and query params
    Given url baseUrl + '/api/v1/clima/buscar'
    And param pais = 'Colombia'
    And param ciudad = 'Medellín'
    # Act: GET
    When method get
    # Assert: 200 and JSON array (may be empty or with items)
    Then status 200
    And match response == '#[]'

  @search-encoded
  Scenario: Search with encoded city name
    # Arrange: city with special character
    Given url baseUrl + '/api/v1/clima/buscar'
    And param pais = 'México'
    And param ciudad = 'Ciudad de México'
    # Act: GET
    When method get
    # Assert: 200
    Then status 200
    And match response == '#[]'
