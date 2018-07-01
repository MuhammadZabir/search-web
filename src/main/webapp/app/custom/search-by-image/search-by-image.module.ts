import { Injectable, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SearchByImageComponent } from './search-by-image.component';
import { SearchByImageService } from './service/search-by-image.service';

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
        path: 'search-by-image',
        component: SearchByImageComponent,
        // resolve: {
        //     pagingParams: SearchByImageResolvePagingParams
        // },
        data: {
            pageTitle: 'searchByImage.home.title'
        }
    }
];

@NgModule({
    imports: [CommonModule, FormsModule, HttpClientModule, RouterModule.forRoot(ROUTES, { useHash: true })],
    providers: [SearchByImageService],
    declarations: [SearchByImageComponent]
})
export class SearchByImageModule {}
