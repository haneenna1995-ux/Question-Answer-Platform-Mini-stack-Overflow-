 describe('Lognin',()=>{
   beforeEach(()=>{
       cy.visit('/login')
   })

     it('shows required errors when submitting empty form', () => {
         cy.get('[data-cy="submit"]').click()

         cy.contains('Email is required').should('be.visible')
         cy.contains('Password is required').should('be.visible')
     })

     it('shows error for invalid credentials',()=>{
         cy.get('[data-cy="email-input"]').type('wrong@exapmle.com')
         cy.get('[data-cy="password-input"]').type('WrongPassword1')
         cy.get('[data-cy="submit"]').click()
         cy.contains('Invalid email or password.').should('be.visible')
     })

     it('sign up link redirect to register page',()=>{
         cy.get('[data-cy="link"]').click()
         cy.url().should('include','/register')
         cy.contains('Create an Account').should('be.visible')
     })










 })