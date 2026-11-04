describe('Registration', () => {

  beforeEach(() => {
    cy.visit('/register')
  })

  it('registers a new user successfully', () => {
    const email = `test${Date.now()}@example.com`

    cy.get('[data-cy="username-input"]').type(`user${Date.now()}`)
    cy.get('[data-cy="email-input"]').type(email)
    cy.get('[data-cy="password-input"]').type('Password1')
    cy.get('[data-cy="submit"]').click()

    cy.contains('Account created. You can now log in.').should('be.visible')
  })

  it('shows required errors when submitting empty form', () => {
    cy.get('button[type="submit"]').click()

    cy.contains('Username is required').should('be.visible')
    cy.contains('Email is required').should('be.visible')
    cy.contains('Password is required').should('be.visible')
  })


  it('shows error for invalid username format', () => {
    cy.get('[data-cy="username-input"]').type('ab')
    cy.get('[data-cy="email-input"]').type('valid@example.com')
    cy.get('[data-cy="password-input"]').type('Password1')
    cy.get('[data-cy="submit"]').click()

    cy.contains('3–20 characters. Letters, numbers, or underscore only.').should('be.visible')
  })

  it('shows error for invalid email format', () => {
    cy.get('[data-cy="username-input"]').type('validUser1')
    cy.get('[data-cy="email-input"]').type('notanemail')
    cy.get('[data-cy="password-input"]').type('Password1')
    cy.get('[data-cy="submit"]').click()

    cy.contains('Enter a valid email address.').should('be.visible')
  })

  it('shows error for weak password', () => {
    cy.get('[data-cy="username-input"]').type('validUser1')
    cy.get('[data-cy="email-input"]').type('valid@example.com')
    cy.get('[data-cy="password-input"]').type('password')
    cy.get('[data-cy="submit"]').click()

    cy.contains('At least 8 characters, with one uppercase, one lowercase, and one number.').should('be.visible')
  })

  it('shows error when email is already registered', () => {
    cy.get('[data-cy="username-input"]').type('validUser1')
    cy.get('[data-cy="email-input"]').type('already@exists.com')
    cy.get('[data-cy="password-input"]').type('Password1')
    cy.get('[data-cy="submit"]').click()
    cy.contains('Email already in use').should('be.visible')
  })

  it('login ling navigate to login page',()=>{
   cy.get('[data-cy="link"]').click()
    cy.url().should('include','/login')

  })

})