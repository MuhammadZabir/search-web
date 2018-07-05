import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SearchWebSharedModule } from 'app/shared';
import {
    HerbComponent,
    HerbViewComponent,
    HerbCreateComponent,
    HerbDeletePopupComponent,
    HerbDeleteComponent,
    herbRoute,
    herbPopupRoute
} from './';

const ENTITY_STATES = [...herbRoute, ...herbPopupRoute];

@NgModule({
    imports: [SearchWebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HerbComponent, HerbViewComponent, HerbCreateComponent, HerbDeleteComponent, HerbDeletePopupComponent],
    entryComponents: [HerbComponent, HerbCreateComponent, HerbDeleteComponent, HerbDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HerbModule {}
