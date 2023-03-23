import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAthlete } from '../athlete.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-athlete-detail',
  templateUrl: './athlete-detail.component.html',
})
export class AthleteDetailComponent implements OnInit {
  athlete: IAthlete | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ athlete }) => {
      this.athlete = athlete;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
