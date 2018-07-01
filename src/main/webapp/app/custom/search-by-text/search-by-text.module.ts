import { Injectable, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SearchByTextComponent } from './search-by-text.component';
import { SearchByImageService } from '../search-by-image/service/search-by-image.service';
import { SearchWebSharedModule } from 'app/shared';

// @Injectable()
// export class SearchByImageResolvePagingParams implements Resolve<any> {

//     constructor(private paginationUtil: JhiPaginationUtil) {
//     }

//     resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//         const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
//         const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'billingMonth,desc';
//         return {
//             page: this.paginationUtil.parsePage(page),
//             predicate: this.paginationUtil.parsePredicate(sort),
//             ascending: this.paginationUtil.parseAscending(sort)
//         };
//     }
// }

const ROUTES: Routes = [
    {
        path: 'search-by-text',
        component: SearchByTextComponent,
        // resolve: {
        //     pagingParams: SearchByImageResolvePagingParams
        // },
        data: {
            pageTitle: 'searchByText.home.title'
        }
    }
];

@NgModule({
    imports: [CommonModule, SearchWebSharedModule, FormsModule, HttpClientModule, RouterModule.forRoot(ROUTES, { useHash: true })],
    providers: [SearchByImageService],
    declarations: [SearchByTextComponent]
})
export class SearchByTextModule {}
