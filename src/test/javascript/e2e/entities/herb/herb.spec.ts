import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { HerbComponentsPage, HerbUpdatePage } from './herb.page-object';

describe('Herb e2e test', () => {
    let navBarPage: NavBarPage;
    let herbUpdatePage: HerbUpdatePage;
    let herbComponentsPage: HerbComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Herbs', () => {
        navBarPage.goToEntity('herb');
        herbComponentsPage = new HerbComponentsPage();
        expect(herbComponentsPage.getTitle()).toMatch(/searchWebApp.herb.home.title/);
    });

    it('should load create Herb page', () => {
        herbComponentsPage.clickOnCreateButton();
        herbUpdatePage = new HerbUpdatePage();
        expect(herbUpdatePage.getPageTitle()).toMatch(/searchWebApp.herb.home.createOrEditLabel/);
        herbUpdatePage.cancel();
    });

    it('should create and save Herbs', () => {
        herbComponentsPage.clickOnCreateButton();
        herbUpdatePage.setNameInput('name');
        expect(herbUpdatePage.getNameInput()).toMatch('name');
        herbUpdatePage.setDescriptionInput('description');
        expect(herbUpdatePage.getDescriptionInput()).toMatch('description');
        herbUpdatePage.save();
        expect(herbUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
