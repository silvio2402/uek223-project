import { Item } from "spring-filter-query-builder"

interface SortQuery {
  sortBy: string[]
  sortDirection?: "asc" | "desc"
}

interface PaginatedQuery {
  page?: number
  size?: number
}

type FilterQuery = Item

export interface QueryParams {
  sort?: SortQuery
  paginate?: PaginatedQuery
  filter?: FilterQuery
}

interface QueryParamsProcessed {
  sort?: string
  page?: string
  size?: string
  filter?: string
}

function buildSortQueryString({ sortBy, sortDirection }: SortQuery): string {
  if (sortBy.length === 0) {
    return ""
  }

  const sortDirectionQuery = sortDirection ? `_${sortDirection}` : ""
  return `${sortBy.join(",")},${sortDirectionQuery}`
}

export function buildQueryParams({
  sort,
  paginate,
  filter,
}: QueryParams): QueryParamsProcessed {
  const queryParams: QueryParamsProcessed = {}

  if (sort) {
    queryParams.sort = buildSortQueryString(sort)
  }

  if (paginate) {
    if (paginate.page !== undefined) {
      queryParams.page = paginate.page.toString()
    }
    if (paginate.size !== undefined) {
      queryParams.size = paginate.size.toString()
    }
  }

  if (filter) {
    queryParams.filter = filter.toString()
  }

  return queryParams
}
