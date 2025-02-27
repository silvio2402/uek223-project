describe("MyListEntry creation", () => {
  it("logs in and creates a MyListEntry", () => {
    // Visit the login page
    cy.visit("http://localhost:5173/login")

    // Enter username and password
    cy.get('input[name="email"]').type("admin@example.com")
    cy.get('input[name="password"]').type("1234")

    // Click the login button
    cy.get('button[aria-label="Login"]').click()

    // Verify successful login (e.g., check for redirect to home page)
    // Verify successful login (e.g., check for redirect to home page)
    cy.get('a[aria-label="Home"]').should("be.visible")

    cy.wait(1000)
    // Visit the MyListEntry page
    cy.visit("http://localhost:5173/mylistentries")

    // Click the plus button to navigate to the creation form
    cy.get('button[aria-label="Add MyListEntry"]').click()

    // Fill in the form fields
    cy.get('input[name="title"]').type("Test Entry Title")

    cy.get('input[name="text"]').type("Test Text")

    // Submit the form
    cy.get('button[type="submit"]').click()

    // verify were back on http://localhost:5173/mylistentries
    cy.url().should("eq", "http://localhost:5173/mylistentries")

    // Verify successful creation (e.g., check for success message)
    cy.contains("Test Entry Title").should("be.visible")
  })
})
