describe('Question',()=>{
    beforeEach(()=>{
    cy.visit('/login')
    cy.get('[data-cy="email-input"]') .type('testuser@example.com')
    cy.get('[data-cy="password-input"]').type('Password1')
    cy.get('[data-cy="submit"]').click()
    cy.url().should('not.include','/login')


    })

    it('shows required errors when form is submitted empty',()=>{

        cy.visit('askQuestion')
        cy.get('[data-cy="submit"]').click();  cy.contains('Title is required').should('be.visible')
        cy.contains('Title is required').should('be.visible')
        cy.contains('Description is required').should('be.visible')
        cy.contains('At least one tag is required').should('be.visible')
    })

    it('create a question with a valid input',()=>{
        cy.visit("/askQuestion")
        cy.get('[data-cy="question-title"]').type('E2E test Question')
        cy.get('[data-cy="question-description"]').type('This is a test description')
        cy.get('[data-cy="tag-input"]').type('ja')
        cy.get('[data-cy="tag-suggestion"]').first().click()
        cy.get('[data-cy="submit"]').click()
        cy.contains('Question created successfully').should('be.visible')
        cy.visit('/myQuestions')
        cy.contains('[data-cy="question-card"]','E2E test Question')
            .find('[data-cy="delete-button"]').click()

    })

    it('updates a question', () => {
        // create a question first
        cy.visit('/askQuestion')
        cy.get('[data-cy="question-title"]').type('Question To Update')
        cy.get('[data-cy="question-description"]').type('Original description.')
        cy.get('[data-cy="tag-input"]').type('ja')
        cy.get('[data-cy="tag-suggestion"]').first().click()
        cy.get('[data-cy="submit"]').click()
        cy.contains('Question created successfully').should('be.visible')

        // update it
        cy.visit('/myQuestions')
        cy.contains('[data-cy="question-card"]', 'Question To Update')
            .find('[data-cy="edit-button"]')
            .click()

        cy.get('[data-cy="edit-title"]').clear().type('Question To Update — Edited')
        cy.get('[data-cy="edit-description"]').clear().type('Updated description.')
        cy.get('[data-cy="tag-input"]').should('be.visible').type('ty')
        cy.get('[data-cy="tag-suggestion"]').first().click()
        cy.wait(500)
        cy.get('[data-cy="save-changes"]').click()
        cy.url().should('include', '/myQuestions')
        cy.contains('Question To Update — Edited').should('be.visible')
        // clean up
        cy.contains('[data-cy="question-card"]', 'Question To Update — Edited')
            .find('[data-cy="delete-button"]')
            .click()
    })

    it('deletes a question', () => {
        // create a question first
        cy.visit('/askQuestion')
        cy.get('[data-cy="question-title"]').type('Question To Delete')
        cy.get('[data-cy="question-description"]').type('This will be deleted.')
        cy.get('[data-cy="tag-input"]').type('ja')
        cy.get('[data-cy="tag-suggestion"]').first().click()
        cy.get('[data-cy="submit"]').click()
        cy.contains('Question created successfully').should('be.visible')

        // delete it
        cy.visit('/myQuestions')
        cy.contains('[data-cy="question-card"]', 'Question To Delete')
            .find('[data-cy="delete-button"]')
            .click()

        cy.contains('Question To Delete').should('not.exist')
    })

})