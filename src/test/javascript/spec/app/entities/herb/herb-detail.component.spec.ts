/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SearchWebTestModule } from '../../../test.module';
import { HerbDetailComponent } from 'app/entities/herb/herb-detail.component';
import { Herb } from 'app/shared/model/herb.model';

describe('Component Tests', () => {
    describe('Herb Management Detail Component', () => {
        let comp: HerbDetailComponent;
        let fixture: ComponentFixture<HerbDetailComponent>;
        const route = ({ data: of({ herb: new Herb(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SearchWebTestModule],
                declarations: [HerbDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HerbDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HerbDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.herb).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
