import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SearchWebSharedModule } from 'app/shared';
import {
    HerbComponent,
    HerbDetailComponent,
    HerbUpdateComponent,
    HerbDeletePopupComponent,
    HerbDeleteDialogComponent,
    herbRoute,
    herbPopupRoute
} from './';

const ENTITY_STATES = [...herbRoute, ...herbPopupRoute];

@NgModule({
    imports: [SearchWebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HerbComponent, HerbDetailComponent, HerbUpdateComponent, HerbDeleteDialogComponent, HerbDeletePopupComponent],
    entryComponents: [HerbComponent, HerbUpdateComponent, HerbDeleteDialogComponent, HerbDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SearchWebHerbModule {}
